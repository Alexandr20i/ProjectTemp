package com.example.ProjectTemp.services;

//import com.example.ProjectTemp.models.GroupMembership;
import com.example.ProjectTemp.models.Post;
import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.models.Group;
import com.example.ProjectTemp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post saveUserPost(String content, User user) {
        Post post = new Post();
        post.setContent(content);
        post.setUser(user);
        post.setCreatedAt(new Date());
        return postRepository.save(post);
    }

    public Post saveGroupPost(String content, User user, Group group) {
        Post post = new Post();
        post.setContent(content);
        post.setUser(user);
        post.setGroup(group);
        post.setCreatedAt(new Date());
        return postRepository.save(post);
    }


    public List<Post> findPostsByUser(User user) {
        return postRepository.findByUser(user);
    }

    public List<Post> findPostById(User user){
        return postRepository.findByUserId(user.getId());
    }

    public List<Post> findPostsByGroup(Group group) {
        return postRepository.findByGroup(group);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }
}