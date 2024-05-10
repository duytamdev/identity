package com.tamstudio.learning.repository;

import com.tamstudio.learning.entity.Permission;
import com.tamstudio.learning.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
