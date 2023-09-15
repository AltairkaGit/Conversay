package com.leopold.service;

import com.leopold.modules.auth.service.AuthService;
import com.leopold.modules.friend.entity.FriendsEntity;
import com.leopold.modules.friend.service.FriendsService;
import com.leopold.modules.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import javax.security.auth.login.CredentialException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FriendsServiceTest {
    private final FriendsService friendsService;
    private final AuthService authService;

    @Autowired
    public FriendsServiceTest(FriendsService friendsService, AuthService authService) {
        this.friendsService = friendsService;
        this.authService = authService;
    }

    @Test
    @Transactional
    void friendsRelsCRUD() throws CredentialException {
        //create me
        String username = "ShelbyGeorge",
                email = "altairka@gmail.com",
                password= "zxc666shelby";
        UserEntity.Gender gender = UserEntity.Gender.female;
        UserEntity me = new UserEntity();
        me.setEmail(email);
        me.setUsername(username);
        me.setPassword(password);
        me.setGender(gender);
        me = authService.registerUser(me);
        //create friend
        username = "ShelbyJohn";
        email = "shelby@zxc.com";
        UserEntity friend = new UserEntity();
        friend.setEmail(email);
        friend.setUsername(username);
        friend.setPassword(password);
        friend.setGender(gender);
        friend = authService.registerUser(friend);
        //send offer from friend to me
        friendsService.sendOffer(friend, me);
        //check offers and accept
        List<UserEntity> myOffers = friendsService.getOffers(me, PageRequest.of(0, 100)).toList();
        assertEquals(myOffers.size(), 1);
        for (UserEntity potentialFriend : myOffers) {
            FriendsEntity offer = friendsService.getOffer(potentialFriend, me);
            assertEquals(offer.getMe(), me);
            assertEquals(offer.getFriend(), potentialFriend);
            assertFalse(offer.isConfirmed());

            friendsService.acceptOffer(offer);

            offer = friendsService.getOffer(potentialFriend, me);
            assertEquals(offer.getMe(), me);
            assertEquals(offer.getFriend(), potentialFriend);
            assertTrue(offer.isConfirmed());
            offer = friendsService.getOffer(me, potentialFriend);
            assertEquals(offer.getMe(), potentialFriend);
            assertEquals(offer.getFriend(), me);
            assertTrue(offer.isConfirmed());
        }

        //check friends
        List<UserEntity> friends = friendsService.getFriends(me, PageRequest.of(0, 100)).toList();
        assertEquals(List.of(friend), friends);
        friends = friendsService.getFriends(friend, PageRequest.of(0, 100)).toList();
        assertEquals(List.of(me), friends);
        //delete friend
        friendsService.deleteFriend(me, friend);
        UserEntity finalMe = me;
        UserEntity finalFriend = friend;
        assertThrows(NoSuchElementException.class, () -> {
            friendsService.getOffer(finalMe, finalFriend);
        });
        assertThrows(NoSuchElementException.class, () -> {
            friendsService.getOffer(finalFriend, finalMe);
        });
        //send offer from friend to me
        friendsService.sendOffer(friend, me);
        FriendsEntity offer = friendsService.getOffer(friend, me);
        friendsService.rejectOffer(offer);
        assertThrows(NoSuchElementException.class, () -> {
            friendsService.getOffer(finalMe, finalFriend);
        });
        assertThrows(NoSuchElementException.class, () -> {
            friendsService.getOffer(finalFriend, finalMe);
        });
    }
}
