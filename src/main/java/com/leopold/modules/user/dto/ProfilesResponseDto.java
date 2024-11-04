package com.leopold.modules.user.dto;

import java.util.List;

public class ProfilesResponseDto {
    List<UserProfileResponseDto> profiles;

    public List<UserProfileResponseDto> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<UserProfileResponseDto> profiles) {
        this.profiles = profiles;
    }
}
