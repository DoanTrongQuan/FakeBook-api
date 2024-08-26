package org.example.identityservice.repository;

import org.example.identityservice.entity.User;
import org.example.identityservice.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRole, Integer> {

    List<UserRole> findAllByUser(User user);
}
