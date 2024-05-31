package com.example.ProjectTemp.repository;

import com.example.ProjectTemp.models.Post;
import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);

    List<Post> findByGroup(Group group);
    List<Post> findByUserId(Long id);
}