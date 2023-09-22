package com.leopold.modules.user.service.impl;

import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.user.dto.UserUpdateRequestDto;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.repos.UserRepository;
import com.leopold.modules.auth.service.AuthService;
import com.leopold.modules.user.service.UserService;
import com.leopold.lib.validator.impl.MaxLengthValidation;
import com.leopold.lib.validator.impl.MinLengthValidation;
import com.leopold.lib.validator.impl.StrictUnicodeEmailValidation;
import com.leopold.lib.validator.Validator;
import com.leopold.lib.validator.impl.ValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService, AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity getUserById(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty()) throw new NoSuchElementException();
        return user.get();
    }

    @Override
    public Set<UserEntity> getUsersByIds(List<Long> ids) {
        return userRepository.findAllByUserIdIn(ids);
    }

    @Override
    public UserEntity getUserByUsername(String userName)  {
        Optional<UserEntity> user = userRepository.findByUsername(userName);
        if (user.isEmpty()) throw new NoSuchElementException();
        return user.get();
    }

    @Override
    public Set<UserEntity> getUsersByUsername(Collection<String> usernames) {
        return userRepository.findAllByUsernameIn(usernames);
    }

    @Override
    public UserEntity updateUsername(UserEntity updatingUser, String username) {
        updatingUser.setUsername(username);
        return userRepository.saveAndFlush(updatingUser);
    }

    @Override
    public UserEntity updateProfilePicture(UserEntity updatingUser, FileEntity picture) {
        updatingUser.setProfilePicture(picture);
        return userRepository.saveAndFlush(updatingUser);
    }

    @Override
    public UserEntity updatePassword(UserEntity updatingUser, String rawPassword) {
        validatePassword(rawPassword);
        updatingUser.setPassword(passwordEncoder.encode(rawPassword));
        return userRepository.saveAndFlush(updatingUser);
    }

    @Override
    public UserEntity updateEmail(UserEntity updatingUser, String email) {
        validateEmail(email);
        updatingUser.setEmail(email);
        return userRepository.saveAndFlush(updatingUser);
    }

    @Override
    public UserEntity registerUser(UserEntity registringUser) throws CredentialException {
        validateEmail(registringUser.getEmail());
        validatePassword(registringUser.getPassword());
        if (userRepository.findByUsername(registringUser.getUsername()).isPresent())
            throw new CredentialException("this username is taken by someone");
        if (userRepository.findByEmail(registringUser.getEmail()).isPresent())
            throw new CredentialException("this email is taken by someone");
        UserEntity user = new UserEntity();
        user.setUsername(registringUser.getUsername());
        user.setEmail(registringUser.getEmail());
        user.setGender(registringUser.getGender());
        user.setPassword(passwordEncoder.encode(registringUser.getPassword()));

        userRepository.saveAndFlush(user);
        return user;
    }

    @Override
    public void deleteUser(UserEntity user) {
        userRepository.deleteById(user.getUserId());
    }

    private void validateEmail(String email) {
        Validator<String> emailValidator = new ValidatorImpl<>(new StrictUnicodeEmailValidation());
        emailValidator.validate(email);
    }

    private void validatePassword(String password) {
        Validator<String> passwordValidator = new ValidatorImpl<>(
                new MinLengthValidation(8).setNextChain(
                new MaxLengthValidation(50)
            ));
        passwordValidator.validate(password);
    }

    private void validateName(String name) {
        Validator<String> nameValidator = new ValidatorImpl<>(
                new MinLengthValidation(2).setNextChain(
                        new MaxLengthValidation(25)
                ));
        nameValidator.validate(name);
    }
}
