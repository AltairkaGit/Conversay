package com.leopold.modules.user.controller;

import com.leopold.modules.user.dto.*;
import com.leopold.modules.user.dto.mapper.UserProfileResponseMapper;
import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.file.service.FileService;
import com.leopold.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserRestControllerV1 {
    private final UserService userService;
    private final FileService fileService;
    private final UserProfileResponseMapper userProfileResponseMapper;

    @Autowired
    public UserRestControllerV1(UserService userService, FileService fileService, UserProfileResponseMapper userProfileResponseMapper) {
        this.userService = userService;
        this.fileService = fileService;
        this.userProfileResponseMapper = userProfileResponseMapper;
    }

    @GetMapping("")
    public ResponseEntity<MyProfileDto> getMyProfile(@RequestAttribute("reqUserId") Long userId) {
        UserEntity user = userService.getUserById(userId);
        MyProfileDto responseDto = userProfileResponseMapper.convertMyProfile(user);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping(value="/{userId}")
    public ResponseEntity<UserProfileResponseDto> getUserProfile(@PathVariable("userId") Long someUserId) {
        UserEntity user = userService.getUserById(someUserId);
        UserProfileResponseDto responseDto = userProfileResponseMapper.convert(user);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value= "/profilePicture")
    public ResponseEntity<UserProfileResponseDto> updateProfilePicture(@RequestAttribute("reqUserId") Long userId, UserUpdateProfilePictureDto dto) {
        UserEntity user = userService.getUserById(userId);
        FileEntity uploadingPicture = fileService.getFile(dto.getPictureUrlOnServer());
        UserEntity updatedUser = userService.updateProfilePicture(user, uploadingPicture);
        UserProfileResponseDto responseDto = userProfileResponseMapper.convert(updatedUser);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value= "/email")
    public ResponseEntity<UserProfileResponseDto> updateEmail(@RequestAttribute("reqUserId") Long userId, UserUpdateEmailDto dto) {
        UserEntity user = userService.getUserById(userId);
        UserEntity updatedUser = userService.updateEmail(user, dto.getEmail());
        UserProfileResponseDto responseDto = userProfileResponseMapper.convert(updatedUser);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value= "/username")
    public ResponseEntity<UserProfileResponseDto> updateUsername(@RequestAttribute("reqUserId") Long userId, UserUpdateUsernameDto dto) {
        UserEntity user = userService.getUserById(userId);
        UserEntity updatedUser = userService.updateUsername(user, dto.getUsername());
        UserProfileResponseDto responseDto = userProfileResponseMapper.convert(updatedUser);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value= "/password")
    public ResponseEntity<UserProfileResponseDto> updateUser(@RequestAttribute("reqUserId") Long userId, UserUpdatePasswordDto dto) {
        UserEntity user = userService.getUserById(userId);
        UserEntity updatedUser = userService.updatePassword(user, dto.getPassword());
        UserProfileResponseDto responseDto = userProfileResponseMapper.convert(updatedUser);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping(value= "")
    public ResponseEntity<HttpStatus> deleteAccount(@RequestAttribute("reqUserId") Long userId) {
        UserEntity user = userService.getUserById(userId);
        userService.deleteUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
