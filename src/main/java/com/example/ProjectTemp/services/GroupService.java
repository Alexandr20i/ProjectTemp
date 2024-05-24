package com.example.ProjectTemp.services;

import com.example.ProjectTemp.models.Group;
import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.controllers.GroupController;
import com.example.ProjectTemp.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

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
}