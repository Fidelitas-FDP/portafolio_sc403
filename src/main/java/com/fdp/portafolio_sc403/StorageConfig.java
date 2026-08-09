package com.fdp.portafolio_sc403;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class StorageConfig {

    @Value("${firebase.json.path}")
    private String jsonPath;

    @Value("${firebase.json.file}")
    private String jsonFile;

    @Bean
    public Storage storage() throws IOException {
        // intentar cargar archivo externo desde ruta de render
        File renderFile = new File("/etc/secrets/firebase-auth.json"); 
        
        // si existe, se usa, sino se usa ClassPathResource
        InputStream inputStream = renderFile.exists()
                ? new FileInputStream(renderFile)
                : new ClassPathResource(jsonPath + File.separator + jsonFile).getInputStream();
        
        try (inputStream) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
            return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        }
    }
}
