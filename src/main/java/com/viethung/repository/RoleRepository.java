package com.viethung.repository;

import com.viethung.entity.ENUM.ERoleName;
import com.viethung.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByRoleName(ERoleName eRoleName);
}
