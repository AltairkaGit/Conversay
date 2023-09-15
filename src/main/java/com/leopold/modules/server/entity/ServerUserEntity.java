package com.leopold.modules.server.entity;

import com.leopold.modules.user.entity.UserEntity;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "server_user")
public class ServerUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "server_user_id")
    private Long serverUserId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "server_id")
    private ServerEntity server;

    public Long getServerUserId() {
        return serverUserId;
    }

    public void setServerUserId(Long serverUserId) {
        this.serverUserId = serverUserId;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ServerEntity getServer() {
        return server;
    }

    public void setServer(ServerEntity server) {
        this.server = server;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerUserEntity that = (ServerUserEntity) o;
        return Objects.equals(serverUserId, that.serverUserId) && Objects.equals(user, that.user) && Objects.equals(server, that.server);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverUserId, user, server);
    }
}
