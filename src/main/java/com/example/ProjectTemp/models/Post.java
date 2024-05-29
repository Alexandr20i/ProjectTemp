package com.example.ProjectTemp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_post")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_author")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_group")
    private Group group;

    @Column(name = "content", nullable = false, length = 10000)
    private String content;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

}