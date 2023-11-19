package com.leopold.modules.server.dto;

import com.leopold.modules.user.dto.UserProfileResponseDto;

public class ServerUserProfileDto {
    private UserProfileResponseDto profile;

    public UserProfileResponseDto getProfile() {
        return profile;
    }

    public void setProfile(UserProfileResponseDto profile) {
        this.profile = profile;
    }
}
