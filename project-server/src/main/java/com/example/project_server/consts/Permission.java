package com.example.project_server.consts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_CREATE("admin:create"),
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin_delete"),
    MANAGER_CREATE("manager:create"),
    MANAGER_READ("manager:read"),
    MANAGER_UPDATE("manager:update"),
    MANAGER_DELETE("manager:delete");

    private final String permission;
}
