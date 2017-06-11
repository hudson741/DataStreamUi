package com.yss.storage;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URI;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {
    private Logger    logger = LoggerFactory.getLogger(FileSystemStorageService.class);
    @Autowired
    StorageProperties storageProperties;

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(storageProperties.getLocation()).toFile());
    }

    @Override
    public void init(String path) {
        try {
            if (Files.notExists(Paths.get(path + storageProperties.getLocation()))) {
                Files.createDirectory(Paths.get(path + storageProperties.getLocation()));
            }
        } catch (Exception e) {
            logger.error("error ", e);
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
    public URI store(String path1, MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }

            init(path1);

            Path path = Paths.get(path1 + storageProperties.getLocation()).resolve(file.getOriginalFilename());

            Files.copy(file.getInputStream(), path);

            return path.toUri();
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }
}
