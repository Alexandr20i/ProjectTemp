package com.example.ProjectTemp.repository;

import com.example.ProjectTemp.models.Follow;
import com.example.ProjectTemp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByUser(User user);
    List<Follow> findByFollower(User follower);
    boolean existsByUserAndFollower(User user, User follower);

    Optional<Follow> findByUserAndFollower(User user, User follower);
}