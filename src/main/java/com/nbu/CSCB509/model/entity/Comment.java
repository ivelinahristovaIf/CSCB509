package com.nbu.CSCB509.model.entity;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    private Post post;

    @OneToOne
    private User user;
}
