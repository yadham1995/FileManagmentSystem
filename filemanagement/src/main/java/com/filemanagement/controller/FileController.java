package com.filemanagement.controller;

import com.filemanagement.exception.AccessDeniedException;
import com.filemanagement.exception.FileStorageException;
import com.filemanagement.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/folders/{folderId}/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<String> createFile(@PathVariable Long folderId,
                                             @RequestHeader HttpHeaders httpHeaders,
                                             @RequestParam("fileData") MultipartFile fileData) throws AccessDeniedException, FileStorageException, IOException {
        String userEmail = Objects.requireNonNull(httpHeaders.get("userEmail")).get(0);
        fileService.createFile(userEmail, folderId, fileData);
        return ResponseEntity.status(HttpStatus.CREATED).body("File is saved Successfully.");
    }

    @PutMapping("/{fileId}")
    public ResponseEntity<String> editFile(@PathVariable long folderId,
                                           @PathVariable Long fileId,
                                           @RequestHeader HttpHeaders httpHeaders,
                                           @RequestParam("fileData") MultipartFile multipartFile) throws AccessDeniedException, FileStorageException, IOException {
        String userEmail = Objects.requireNonNull(httpHeaders.get("userEmail")).get(0);
        fileService.editFile(userEmail, fileId, multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).body("File is updated Successfully.");

    }
}
