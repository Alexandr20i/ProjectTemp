package com.example.ProjectTemp.repository;

import com.example.ProjectTemp.models.FollowUser;
import com.example.ProjectTemp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowUser, Long> {
    List<FollowUser> findByUser(User user);
    List<FollowUser> findByFollower(User follower);

    //существует ли запись в таблице между user и follower
    boolean existsByUserAndFollower(User user, User follower);

    // метод ищет запись в таблице
    Optional<FollowUser> findByUserAndFollower(User user, User follower);

    @Transactional
    void deleteByUser(User user);

    @Transactional
    void deleteByFollower(User follower);
}