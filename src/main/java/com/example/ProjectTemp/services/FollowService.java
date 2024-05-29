package com.example.ProjectTemp.services;

import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.models.Follow;
import com.example.ProjectTemp.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    // Подписаться на пользователя
    public void followUser(User user, User follower) {
        if (!followRepository.existsByUserAndFollower(user, follower)) {
            Follow follow = new Follow();
            follow.setUser(user);
            follow.setFollower(follower);
            followRepository.save(follow);
        }
    }

    // Отписаться от пользователя
    public void unfollowUser(User user, User follower) {
        Follow follow = followRepository.findByUserAndFollower(user, follower).orElse(null);
        if (follow != null) {
            followRepository.delete(follow);
        }
    }

    // Получить всех пользователей, на которых подписан пользователь
    public List<User> getFollowingUsers(User follower) {
        return followRepository.findByFollower(follower).stream()
                .map(Follow::getUser)
                .collect(Collectors.toList());
    }

    // Получить всех подписчиков пользователя
    public List<User> getFollowers(User user) {
        return followRepository.findByUser(user).stream()
                .map(Follow::getFollower)
                .collect(Collectors.toList());
    }

    // Проверка, подписан ли один пользователь на другого
    public boolean isFollowing(User user, User follower) {
        return followRepository.existsByUserAndFollower(user, follower);
    }
}