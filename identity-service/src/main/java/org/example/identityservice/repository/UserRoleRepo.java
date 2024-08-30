package org.example.identityservice.repository;

import org.example.identityservice.entity.User;
import org.example.identityservice.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRoleRepo  extends JpaRepository<UserRole, Integer> {

    Set<UserRole> findAllByUser(User user);


}
