package com.tju.bclab.vote_backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    String uploadImg(MultipartFile file);
    String uploadPdf(MultipartFile file);
}
