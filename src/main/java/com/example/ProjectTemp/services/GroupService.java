package com.example.ProjectTemp.services;

import com.example.ProjectTemp.models.Group;
import com.example.ProjectTemp.models.GroupSubscription;
import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.controllers.GroupController;
import com.example.ProjectTemp.repository.GroupRepository;
import com.example.ProjectTemp.repository.GroupSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupSubscriptionRepository groupSubscriptionRepository;


    public Group createGroup(String name, User owner) {
        Group group = new Group();
        group.setName(name);
        group.setOwner(owner);
        return groupRepository.save(group);
    }

    public List<Group> searchGroupsByName(String name) {
        return groupRepository.findByNameContaining(name);
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public void subscribeToGroup(User user, Group group) {
        if (!groupSubscriptionRepository.existsByGroupAndUser(group, user)) {
            GroupSubscription subscription = new GroupSubscription();
            subscription.setGroup(group);
            subscription.setUser(user);
            groupSubscriptionRepository.save(subscription);
        }
    }

    public void unsubscribeFromGroup(User user, Group group) {
        GroupSubscription subscription = groupSubscriptionRepository.findByGroupAndUser(group, user);
        if (subscription != null) {
            groupSubscriptionRepository.delete(subscription);
        }
    }
    public boolean isUserSubscribed(User user, Group group) {
        return groupSubscriptionRepository.existsByGroupAndUser(group, user);
    }

    public void updateGroupDetails(Group group, String name, String description) {
        group.setName(name);
        group.setDescription(description);
        groupRepository.save(group);
    }

    public void deleteGroup(Group group) {
        groupRepository.delete(group);
    }
}