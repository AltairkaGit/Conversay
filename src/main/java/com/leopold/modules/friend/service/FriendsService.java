package com.leopold.modules.friend.service;

import com.leopold.modules.friend.entity.FriendsEntity;
import com.leopold.modules.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Transactional
public interface FriendsService {
    Page<UserEntity> getFriends(UserEntity user, Pageable pageable);
    Page<UserEntity> getOffers(UserEntity user, Pageable pageable);
    void sendOffer(UserEntity from, UserEntity to);
    FriendsEntity getOffer(UserEntity from, UserEntity to);
    void deleteFriend(UserEntity from, UserEntity to);
    void acceptOffer(FriendsEntity offer);
    void rejectOffer(FriendsEntity offer);

}
