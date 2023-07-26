package com.filemanagement.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateFileRequest {
    private String name;
    private MultipartFile fileData;
}
