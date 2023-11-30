package com.viethung.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
    String handleUpload(MultipartFile file);
}
