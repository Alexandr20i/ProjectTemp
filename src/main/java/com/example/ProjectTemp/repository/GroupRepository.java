package com.example.ProjectTemp.repository;

import com.example.ProjectTemp.models.Group;
import com.example.ProjectTemp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByNameContaining(String name);

    List<Group> findByOwner(User owner);

//    @Transactional
//    void deleteByUser(User user);



}