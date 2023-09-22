package com.leopold.modules.friend.controller;

import com.leopold.modules.friend.service.FriendsService;
import com.leopold.modules.user.dto.UserProfileResponseDto;
import com.leopold.modules.user.dto.mapper.UserProfileResponseMapper;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friends")
public class FriendsRestControllerV1 {
    private final FriendsService friendsService;
    private final UserService userService;
    private final UserProfileResponseMapper userProfileResponseMapper;

    @Autowired
    public FriendsRestControllerV1(FriendsService friendsService, UserService userService, UserProfileResponseMapper userProfileResponseMapper) {
        this.friendsService = friendsService;
        this.userService = userService;
        this.userProfileResponseMapper = userProfileResponseMapper;
    }

    @GetMapping(value="")
    public ResponseEntity<Page<UserProfileResponseDto>> getFriends(
            @RequestAttribute("reqUserId") Long userId,
            Pageable pageable
    ) {
        UserEntity me = userService.getUserById(userId);
        Page<UserEntity> paged = friendsService.getFriends(me, pageable);
        Page<UserProfileResponseDto> res = userProfileResponseMapper.convertPage(paged);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value="/offers")
    public ResponseEntity<Page<UserProfileResponseDto>> getOffers(
            @RequestAttribute("reqUserId") Long userId,
            Pageable pageable
    ) {
        UserEntity me = userService.getUserById(userId);
        Page<UserEntity> paged = friendsService.getOffers(me, pageable);
        Page<UserProfileResponseDto> res = userProfileResponseMapper.convertPage(paged);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
