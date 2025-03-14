package com.kamalteja.brevify.shortenerService.dao;

import com.kamalteja.brevify.shortenerService.model.CodeUrlMapping;

public interface ICodeUrlMappingDAO {
    CodeUrlMapping save(CodeUrlMapping codeUrlMapping);

    CodeUrlMapping findByShortCode(String shortCode);
}
