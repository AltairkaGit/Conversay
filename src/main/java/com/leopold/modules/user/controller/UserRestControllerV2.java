package com.leopold.modules.user.controller;

import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.file.service.FileService;
import com.leopold.modules.user.dto.*;
import com.leopold.modules.user.dto.mapper.UserProfileResponseMapper;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v2/user")
public class UserRestControllerV2 {
    private final UserService userService;
    private final FileService fileService;
    private final UserProfileResponseMapper userProfileResponseMapper;

    @Autowired
    public UserRestControllerV2(UserService userService, FileService fileService, UserProfileResponseMapper userProfileResponseMapper) {
        this.userService = userService;
        this.fileService = fileService;
        this.userProfileResponseMapper = userProfileResponseMapper;
    }

    @GetMapping("")
    @Operation(summary = "get your profile with additional data")
    public ResponseEntity<MyProfileDto> getMyProfile(@RequestAttribute("reqUserId") Long userId) {
        UserEntity user = userService.getUserById(userId);
        MyProfileDto responseDto = userProfileResponseMapper.convertMyProfile(user);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping(value="/{userId}")
    @Operation(summary = "get user profile with basic data")
    public ResponseEntity<UserProfileResponseDto> getUserProfile(@PathVariable("userId") Long someUserId) {
        UserEntity user = userService.getUserById(someUserId);
        UserProfileResponseDto responseDto = userProfileResponseMapper.convert(user);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value= "/profilePicture")
    @Operation(summary = "upload a multipart file, 200 and FileResponseDto if ok, 400, 500 otherwise")
    public ResponseEntity<MyProfileDto> updateProfilePicture(
            @RequestAttribute("reqUserId") Long userId,
            @RequestPart("file") MultipartFile file
    ) {
        FileEntity fileEntity = fileService.uploadFile(file);
        UserEntity user = userService.getUserById(userId);
        UserEntity updatedUser = userService.updateProfilePicture(user, fileEntity);
        MyProfileDto responseDto = userProfileResponseMapper.convertMyProfile(updatedUser);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value= "/email")
    @Operation(summary = "update email")
    public ResponseEntity<MyProfileDto> updateEmail(
            @RequestAttribute("reqUserId") Long userId,
            @RequestBody UserUpdateEmailDto dto
    ) {
        UserEntity user = userService.getUserById(userId);
        UserEntity updatedUser = userService.updateEmail(user, dto.getEmail());
        MyProfileDto responseDto = userProfileResponseMapper.convertMyProfile(updatedUser);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value= "/username")
    @Operation(summary = "update username")
    public ResponseEntity<MyProfileDto> updateUsername(
            @RequestAttribute("reqUserId") Long userId,
            @RequestBody UserUpdateUsernameDto dto
    ) {
        UserEntity user = userService.getUserById(userId);
        UserEntity updatedUser = userService.updateUsername(user, dto.getUsername());
        MyProfileDto responseDto = userProfileResponseMapper.convertMyProfile(updatedUser);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping(value= "/password")
    @Operation(summary = "update password")
    public ResponseEntity<Void> updateUser(
            @RequestAttribute("reqUserId") Long userId,
            @RequestBody UserUpdatePasswordDto dto
    ) {
        UserEntity user = userService.getUserById(userId);
        UserEntity updatedUser = userService.updatePassword(user, dto.getPassword());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value= "")
    @Operation(summary = "delete account forever")
    public ResponseEntity<Void> deleteAccount(@RequestAttribute("reqUserId") Long userId) {
        UserEntity user = userService.getUserById(userId);
        userService.deleteUser(user);
        return ResponseEntity.ok().build();
    }
}
