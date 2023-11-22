package com.leopold.modules.server.dto;

import com.leopold.modules.user.dto.UserProfileResponseDto;

public class ServerUserProfileDto extends UserProfileResponseDto {
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
