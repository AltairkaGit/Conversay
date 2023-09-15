package com.leopold.modules.appRole.repos;

import com.leopold.modules.appRole.entity.AppRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRoleEntity, Long> {
}
