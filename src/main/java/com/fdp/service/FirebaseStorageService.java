package com.fdp.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author AaCcAio
 */
@Service
public class FirebaseStorageService {
    @Value("${firebase.bucket.name}")
    private String bucketName;
    
    @Value("${firebase.storage.path}")
    private String storagePath;
    
    // Aqui se inyecta al cliente desde el storage con bean
    private final Storage storage;
    
    public FirebaseStorageService(Storage storage) {
        this.storage = storage;
    }
    
    // Sube archivo imagen desde almacenamiento firebase
    public String uploadImage(MultipartFile localFile, String folder, Integer id) throws IOException {
        String originalName = localFile.getOriginalFilename();
        String fileExtension = "";
        
        if (originalName != null && originalName.contains(".")) {
            fileExtension = originalName.substring(originalName.lastIndexOf("."));
        }
        
        // Genera nombre de archivo con formato estandarizado
        String fileName = "img" + getFormattedNumber(id) + fileExtension;
        
        File tempFile = convertToFile(localFile);
               
        try {
            return uploadToFirebase(tempFile, folder, fileName);
        } finally {
            // elimina archivo temporal
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    // Convierte multipartFile a archivo temporal en servidor
    private File convertToFile(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("upload-", ".tmp");
        
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        
        return tempFile;   
    }
    
    // sube archivo a firebase y genera URL firmada    
    private String uploadToFirebase(File file, String folder, String fileName) throws IOException {
        // define ID del blob e info.
        BlobId blobId = BlobId.of(bucketName, storagePath + "/" + folder + "/" + fileName);
        String mimeType = Files.probeContentType(file.toPath());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(mimeType != null ? mimeType : "media").build();
        
        // sube archivo, Objeto 'storage' debe tener credenciales condiguradas
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        
        // genera URL firmada que caduce en 5 años
        return storage.signUrl(blobInfo, 1825, TimeUnit.DAYS).toString();
    }

    /*
    * Genera string numérico de 14 digitos rellenando con ceros a izq.
    */
    private String getFormattedNumber(long id) { // Long para que sea compatible con 14 digitos
        return String.format("%014d", id);
    }

    
}
