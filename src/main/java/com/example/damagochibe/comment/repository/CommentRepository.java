package com.example.damagochibe.comment.repository;

import com.example.damagochibe.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository <Comment, Long> {
//    List<Comment> findAllByPostId(Long postId);`
  //  List<Comment> findAllByMemberId(Long memberId);

    //void deleteByCommentId(Long commentId);
}
