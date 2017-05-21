package com.yss.storage;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URI;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {
    @Autowired
    StorageProperties storageProperties;

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(storageProperties.getLocation()).toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(Paths.get(storageProperties.getLocation()));
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public Path load(String filename) {
        return Paths.get(storageProperties.getLocation()).resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path     file     = load(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public URI store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }

            Path path = Paths.get(storageProperties.getLocation()).resolve(file.getOriginalFilename());

            Files.copy(file.getInputStream(), path);

            return path.toUri();
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
