package com.viethung.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

@Service
public class UploadFileServiceImpl {
    @Value("${upload.path}")
    private String uploadPath;

    public String handleUpload(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        try {
            byte[] bytes = file.getBytes();

            String fileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename().split("\\.")[1];
            Path path = Paths.get(uploadPath + fileName);

            Files.write(path,bytes);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
