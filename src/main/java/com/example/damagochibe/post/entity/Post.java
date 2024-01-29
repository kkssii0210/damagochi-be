package com.example.damagochibe.post.entity;

import com.example.damagochibe.comment.entity.Comment;
import com.example.damagochibe.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(schema = "damagochi")
@NoArgsConstructor
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(length = 1000, nullable = false)
    private String title;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String content;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("createdDate asc ") // 댓글 정렬
    private List<Comment> comments;
    /* 게시글 수정 메소드 */
    public void update (String title, String content) {
        this.title = title;
        this.content = content;
    }
}

