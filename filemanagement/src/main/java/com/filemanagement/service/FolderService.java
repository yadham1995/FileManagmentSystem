package com.filemanagement.service;

import com.filemanagement.entity.Item;
import com.filemanagement.util.ItemType;
import com.filemanagement.exception.AccessDeniedException;
import com.filemanagement.repository.ItemRepository;
import com.filemanagement.util.CheckUserUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class FolderService {
    private final ItemRepository itemRepository;
    private final CheckUserUtil checkUserUtil;

    public FolderService(ItemRepository itemRepository, CheckUserUtil checkUserUtil) {
        this.itemRepository = itemRepository;
        this.checkUserUtil = checkUserUtil;
    }

    public Object createFolder(String userEmail, String name, Long spaceId) throws EntityNotFoundException, AccessDeniedException {
        Item folder = new Item();
        Item space = itemRepository.findById(spaceId)
                .orElseThrow(() -> new EntityNotFoundException("Space not found"));

        // Check Authorization
        checkUserUtil.validateUserPermission(userEmail, space);

        folder.setType(ItemType.FOLDER.getName());
        folder.setName(name);
        folder.setPermissionGroup(space.getPermissionGroup());
        return itemRepository.save(folder);

    }

    public Item editFolder(String userEmail, String name, Long folderId) throws EntityNotFoundException, AccessDeniedException {
        Item folder = null;
        folder = itemRepository.findById(folderId)
                .orElseThrow(() -> new EntityNotFoundException("Folder not found"));

        // Check Authorization
        checkUserUtil.validateUserPermission(userEmail, folder);
        folder.setName(name);
        return itemRepository.save(folder);
    }

}
