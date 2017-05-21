package com.yss.storage;

import java.net.URI;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void deleteAll();

    void init();

    Path load(String filename);

    Resource loadAsResource(String filename);

    URI store(MultipartFile file);
}


//~ Formatted by Jindent --- http://www.jindent.com
