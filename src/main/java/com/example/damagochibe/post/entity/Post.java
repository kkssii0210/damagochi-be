package com.example.damagochibe.post.entity;

import com.example.damagochibe.comment.entity.Comment;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "damagochi")
@NoArgsConstructor
@Getter @Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;
    private String title;

    @Column(name="content", length = 3000)
    private String content;

    @OneToMany(mappedBy = "post")
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();


    public Post(Long id, String title, String content, List<Comment> comments){
        this.id = id;
        this.title = title;
        this.content = content;
        this.comments = comments;
    }
}
