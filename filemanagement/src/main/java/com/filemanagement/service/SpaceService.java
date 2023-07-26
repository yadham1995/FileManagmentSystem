package com.filemanagement.service;

import com.filemanagement.entity.Item;
import com.filemanagement.entity.PermissionGroup;
import com.filemanagement.util.ItemType;
import com.filemanagement.exception.AccessDeniedException;
import com.filemanagement.repository.ItemRepository;
import com.filemanagement.repository.PermissionGroupRepository;
import com.filemanagement.util.CheckUserUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class SpaceService {
    private final ItemRepository itemRepository;
    private final CheckUserUtil checkUserUtil;
    private final PermissionGroupRepository permissionGroupRepository;

    public SpaceService(ItemRepository itemRepository, CheckUserUtil checkUserUtil, PermissionGroupRepository permissionGroupRepository) {
        this.itemRepository = itemRepository;
        this.checkUserUtil = checkUserUtil;
        this.permissionGroupRepository = permissionGroupRepository;
    }

    public Item createSpace(String name, String groupName) throws Exception {
        Item space = new Item();
        return getItem(name, groupName, space);
    }

    public Item editSpace(String userEmail, Long spaceId, String name, String groupName) throws EntityNotFoundException, AccessDeniedException {
        Item space = null;
        space = itemRepository.findById(spaceId)
                .orElseThrow(() -> new EntityNotFoundException("Space not found"));

        // Check Authorization
        checkUserUtil.validateUserPermission(userEmail, space);

        return getItem(name, groupName, space);
    }

    private Item getItem(String name, String groupName, Item space) {
        PermissionGroup permissionGroup = new PermissionGroup();
        permissionGroup.setGroupName(groupName);

        space.setPermissionGroup(permissionGroup);
        space.setType(ItemType.SPACE.getName());
        space.setName(name);
        space.setPermissionGroup(permissionGroup);

        return itemRepository.save(space);
    }

}