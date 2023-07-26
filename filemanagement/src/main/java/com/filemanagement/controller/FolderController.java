package com.filemanagement.controller;

import com.filemanagement.dto.FolderRequest;
import com.filemanagement.entity.Item;
import com.filemanagement.exception.AccessDeniedException;
import com.filemanagement.service.FolderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

@RestController
@RequestMapping("/spaces/{spaceId}/folders")
public class FolderController {
    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping
    public ResponseEntity<Item> createFolder(
            @PathVariable Long spaceId,
            @RequestHeader HttpHeaders httpHeaders,
            @RequestBody FolderRequest request) throws EntityNotFoundException, AccessDeniedException {
        String userEmail = Objects.requireNonNull(httpHeaders.get("userEmail")).get(0);
        Item folder = (Item) folderService.createFolder(userEmail, request.getName(), spaceId);
        return ResponseEntity.status(HttpStatus.CREATED).body(folder);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> editFolder(@PathVariable Long folderId,
                                           @RequestBody FolderRequest folderRequest,
                                           @RequestHeader HttpHeaders httpHeaders) throws EntityNotFoundException, AccessDeniedException  {
        String userEmail = Objects.requireNonNull(httpHeaders.get("userEmail")).get(0);
        Item updatedFolder = folderService.editFolder(userEmail, folderRequest.getName(), folderId);
        return ResponseEntity.ok(updatedFolder);
    }
}