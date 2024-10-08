package com.example.profileservice.repository;

import com.example.profileservice.entity.UserProfile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepo extends Neo4jRepository<UserProfile, String> {
    UserProfile findByUserId(String userId);
}
