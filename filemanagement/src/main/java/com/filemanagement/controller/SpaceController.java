package com.filemanagement.controller;

import com.filemanagement.dto.CreateSpaceRequest;
import com.filemanagement.entity.Item;
import com.filemanagement.exception.AccessDeniedException;
import com.filemanagement.service.SpaceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

@RestController
@RequestMapping("/spaces")
public class SpaceController {
    private final SpaceService spaceService;

    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @PostMapping("/create")
    public ResponseEntity<Item> createSpace(@RequestBody CreateSpaceRequest request) throws Exception {
        Item space = spaceService.createSpace(request.getName(), request.getGroupName());
        return ResponseEntity.status(HttpStatus.CREATED).body(space);
    }

    @PutMapping("/{spaceId}")
    public ResponseEntity<Item> editSpace(@PathVariable Long spaceId,
                                          @RequestBody CreateSpaceRequest createSpaceRequest,
                                          @RequestHeader HttpHeaders httpHeaders) throws EntityNotFoundException, AccessDeniedException {
        String userEmail = Objects.requireNonNull(httpHeaders.get("userEmail")).get(0);
        Item updatedSpace = spaceService.editSpace(userEmail, spaceId, createSpaceRequest.getName(), createSpaceRequest.getGroupName());
        return ResponseEntity.ok(updatedSpace);
    }
}