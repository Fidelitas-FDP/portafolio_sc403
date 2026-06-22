package com.fdp.service;

import domain.Categoria;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.CategoriaRepository;

@Service
public class CategoriaService {
    
    // final por inmutable
    private final CategoriaRepository categoriaRepository;
    
    // inyecta constructor, sin req. de @Autowired segun version de spring
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) {
            categoriaRepository.findByActivoTrue();
        }

        return categoriaRepository.findAll();
    }
    
}
