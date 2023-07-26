package com.filemanagement.service;

import com.filemanagement.entity.File;
import com.filemanagement.entity.Item;
import com.filemanagement.util.ItemType;
import com.filemanagement.exception.AccessDeniedException;
import com.filemanagement.exception.FileStorageException;
import com.filemanagement.repository.FileRepository;
import com.filemanagement.repository.ItemRepository;
import com.filemanagement.util.CheckUserUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Objects;

@Service
@Transactional
public class FileService {
    private final ItemRepository itemRepository;
    private final FileRepository fileRepository;
    private final CheckUserUtil checkUserUtil;


    public FileService(ItemRepository itemRepository, FileRepository fileRepository, CheckUserUtil checkUserUtil) {
        this.itemRepository = itemRepository;
        this.fileRepository = fileRepository;
        this.checkUserUtil = checkUserUtil;
    }

    public void createFile(String userEmail, Long folderId, MultipartFile fileData) throws EntityNotFoundException, AccessDeniedException, FileStorageException, IOException {
        File fileEntity = new File();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(fileData.getOriginalFilename()));

        Item folder = itemRepository.findById(folderId).orElseThrow(()
                -> new EntityNotFoundException("Folder not found"));

        // Check Authorization
        checkUserUtil.validateUserPermission(userEmail, folder);

        Item file = new Item();

        file.setType(ItemType.FILE.getName());
        file.setName(fileName);
        file.setPermissionGroup(folder.getPermissionGroup());

        fileEntity.setFile_data(fileData.getBytes());
        fileEntity.setItem(file);

        fileRepository.save(fileEntity);
    }

    public void editFile(String userEmail, Long fileId, MultipartFile multipartFile) throws EntityNotFoundException, AccessDeniedException, FileStorageException, IOException {
        File file = null;
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileStorageException("File not found"));
        checkUserUtil.validateUserPermission(userEmail, file.getItem());
        file.setFile_data(multipartFile.getBytes());
        file.getItem().setName(fileName);

        if (file == null)
            throw new FileStorageException("Error happened while saving file!");

        fileRepository.save(file);
    }

}