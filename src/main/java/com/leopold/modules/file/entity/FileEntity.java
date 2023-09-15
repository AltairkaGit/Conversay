package com.leopold.modules.file.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.leopold.modules.server.entity.ServerEntity;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.chat.entity.ChatEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "file")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id", nullable = false)
    private Long fileId;

    @Column(name = "filename", nullable = false)
    private String name;

    @Column(name = "file_size", nullable = false)
    private Integer size;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @Column(name = "url", nullable = false)
    private String url;

    @OneToOne(mappedBy = "profilePicture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private UserEntity profilePictureOfUser;

    @OneToOne(mappedBy = "serverPicture", fetch = FetchType.LAZY)
    @JsonBackReference
    private ServerEntity serverPicture;

    @OneToOne(mappedBy = "chatPicture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private ChatEntity chatPicture;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ServerEntity getServerPicture() {
        return serverPicture;
    }

    public void setServerPicture(ServerEntity serverPicture) {
        this.serverPicture = serverPicture;
    }

    public ChatEntity getChatPicture() {
        return chatPicture;
    }

    public void setChatPicture(ChatEntity chatPicture) {
        this.chatPicture = chatPicture;
    }

    public UserEntity getProfilePictureOfUser() {
        return profilePictureOfUser;
    }
    public void setProfilePictureOfUser(UserEntity profilePictureOfUser) {
        this.profilePictureOfUser = profilePictureOfUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileEntity that = (FileEntity) o;

        if (!Objects.equals(fileId, that.fileId)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(size, that.size)) return false;
        if (!Objects.equals(mimeType, that.mimeType)) return false;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        int result = fileId != null ? fileId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
