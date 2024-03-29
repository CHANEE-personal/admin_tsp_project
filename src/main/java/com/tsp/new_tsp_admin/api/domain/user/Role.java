package com.tsp.new_tsp_admin.api.domain.user;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_ADMIN("슈퍼관리자"),
    ROLE_MANAGER("관리자"),
    ROLE_USER("일반유저");

    private final String description;

    Role(String description) {
        this.description = description;
    }
}
