package com.example.TradeBot.service.staticResource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StaticResourceServiceImpl implements StaticResourceService{
    @Override
    public Resource load(String filename) {
        log.info("getting resource file by name [{}]", filename);
        Resource resource = new ClassPathResource("/static/" + filename);
        if (resource.exists() || resource.isReadable()) {
            log.info("successfully got resource file by name [{}]", filename);
            return resource;
        } else {
            log.warn("could not load the file: [{}]", filename);
            throw new RuntimeException("Could not read the file!");
        }
    }
}
