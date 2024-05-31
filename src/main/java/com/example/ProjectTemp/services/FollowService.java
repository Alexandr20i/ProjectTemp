package com.example.ProjectTemp.services;

import com.example.ProjectTemp.models.FollowUser;
import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    /** Подписаться на пользователя */
    public void followUser(User user, User follower) {
        if (!followRepository.existsByUserAndFollower(user, follower)) {
            FollowUser followUser = new FollowUser();
            followUser.setUser(user);
            followUser.setFollower(follower);
            followRepository.save(followUser);
        }
    }

    /** Отписаться от пользователя */
    public void unfollowUser(User user, User follower) {
        FollowUser followUser = followRepository.findByUserAndFollower(user, follower).orElse(null);
        if (followUser != null) {
            followRepository.delete(followUser);
        }
    }

    /** Получить всех пользователей, на которых подписан пользователь */
    public List<User> getFollowingUsers(User follower) {
        return followRepository.findByFollower(follower).stream()
                .map(com.example.ProjectTemp.models.FollowUser::getUser)
                .collect(Collectors.toList());
    }

    /** Получить всех подписчиков пользователя */
    public List<User> getFollowers(User user) {
        return followRepository.findByUser(user).stream()
                .map(com.example.ProjectTemp.models.FollowUser::getFollower)
                .collect(Collectors.toList());
    }

    /** Проверка, подписан ли один пользователь на другого */
    public boolean isFollowing(User user, User follower) {
        return followRepository.existsByUserAndFollower(user, follower);
    }
}