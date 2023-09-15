package com.leopold.modules.auth.service;

import com.leopold.modules.user.entity.UserEntity;
import jakarta.transaction.Transactional;

import javax.security.auth.login.CredentialException;

@Transactional
public interface AuthService {
    UserEntity registerUser(UserEntity registringUser) throws CredentialException;
}
