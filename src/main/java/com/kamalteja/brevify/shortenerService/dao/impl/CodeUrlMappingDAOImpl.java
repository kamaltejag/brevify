package com.kamalteja.brevify.shortenerService.dao.impl;

import com.kamalteja.brevify.shortenerService.dao.ICodeUrlMappingDAO;
import com.kamalteja.brevify.shortenerService.model.CodeUrlMapping;
import com.kamalteja.brevify.shortenerService.repository.CodeUrlMappingRepository;
import org.springframework.stereotype.Service;

@Service
public class CodeUrlMappingDAOImpl implements ICodeUrlMappingDAO {

    private final CodeUrlMappingRepository codeUrlMappingRepository;

    public CodeUrlMappingDAOImpl(CodeUrlMappingRepository codeUrlMappingRepository) {
        this.codeUrlMappingRepository = codeUrlMappingRepository;
    }

    @Override
    public CodeUrlMapping save(CodeUrlMapping codeUrlMapping) {
        return codeUrlMappingRepository.save(codeUrlMapping);
    }

    @Override
    public CodeUrlMapping findByShortCode(String shortCode) {
        return codeUrlMappingRepository.findByShortCode(shortCode).orElse(null);
    }
}
