package com.filemanagement.util;

import com.filemanagement.entity.Item;
import com.filemanagement.entity.Permission;
import com.filemanagement.exception.AccessDeniedException;
import com.filemanagement.repository.PermissionRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CheckUserUtil {
    private final PermissionRepository permissionRepository;

    public CheckUserUtil(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public void validateUserPermission(String email, Item item) {
        if (!Objects.equals(item.getPermissionGroup().getId(), getUserPermissionId(email)))
            throw new AccessDeniedException("User is not Authorized to this action");
    }

    @SneakyThrows
    public Long getUserPermissionId(String userEmail) {
        Permission permission = permissionRepository.findByUserEmail(userEmail);
        if (permission != null && permission.getGroup() != null && permission.getGroup().getId() != null)
            return permission.getGroup().getId();
        else
            return null;
    }
}
