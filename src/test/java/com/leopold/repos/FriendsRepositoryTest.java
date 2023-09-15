package com.leopold.repos;

import com.leopold.modules.friend.repos.FriendsRepository;
import com.leopold.modules.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FriendsRepositoryTest {
    private final FriendsRepository friendsRepository;

    @Autowired
    public FriendsRepositoryTest(FriendsRepository friendsRepository) {
        this.friendsRepository = friendsRepository;
    }

    @Test
    void getConfirmedFriends() {
        Pageable pageable = PageRequest.of(0, 100);
        Page<UserEntity> friendsPage = friendsRepository.findFriendsByUserId(1L, pageable);
        Page<UserEntity> offersPage = friendsRepository.findOffersByUserId(1L, pageable);
        List<UserEntity> friends = friendsPage.getContent();
        List<UserEntity> offers = offersPage.getContent();
        //it will fail when there are more items inside, now it's ok
        assertEquals(3, friends.size());
        assertEquals(1, offers.size());
    }
}
