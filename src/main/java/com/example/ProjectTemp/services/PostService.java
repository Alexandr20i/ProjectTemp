package com.example.ProjectTemp.services;

import com.example.ProjectTemp.models.Post;
import com.example.ProjectTemp.models.User;
import com.example.ProjectTemp.models.Group;
import com.example.ProjectTemp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post savePost(String content, User user) {
        Post post = new Post();
        post.setContent(content);
        post.setUser(user);
        post.setCreatedAt(new Date());
        return postRepository.save(post);
    }

    public Post createPostForGroup(String content, Group group) {
        Post post = new Post();
        post.setContent(content);
        post.setGroup(group);
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
}