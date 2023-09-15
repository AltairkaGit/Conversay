package com.leopold.repos;

import com.leopold.modules.server.entity.ServerEntity;
import com.leopold.modules.server.repos.ServerRepository;
import com.leopold.modules.server.repos.ServerUserRepository;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ServerUserRepositoryTest {
    private final ServerUserRepository serverUserRepository;
    private final ServerRepository serverRepository;
    private final UserRepository userRepository;

    @Autowired
    public ServerUserRepositoryTest(ServerUserRepository serverUserRepository, ServerRepository serverRepository, UserRepository userRepository) {
        this.serverUserRepository = serverUserRepository;
        this.serverRepository = serverRepository;
        this.userRepository = userRepository;
    }

    @Test
    void getUserServers() {
        Pageable pageable = PageRequest.of(0, 3);
        UserEntity user = userRepository.findByUsername("Altairka").get();
        List<ServerEntity> servers = serverUserRepository.findUserServers(user, pageable).getContent();

        assertEquals(3, servers.size());
    }

    @Test
    void getServerUsers() {
        Pageable pageable = PageRequest.of(0, 100);
        ServerEntity server = serverRepository.findById(1L).get();
        List<UserEntity> users = serverUserRepository.findServerUsers(server, pageable).getContent();

        assertEquals(4, users.size());
    }
}
