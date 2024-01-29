//package com.example.damagochibe.comment.service;
//
//import com.example.damagochibe.comment.entity.Comment;
//import com.example.damagochibe.comment.repository.CommentRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CommentService {
//    private final CommentRepository commentRepository;
////생성자 주입을 통한 의존성 주입
//    public CommentService(CommentRepository commentRepository){
//        this.commentRepository = commentRepository;
//    }
//
//
//    //게시물 아이디와 멤버아이디를 통해 조회
//    public List<Comment> getAllCommentByPostId(Long postId){
//        return commentRepository.findAllByPostId(postId);
//    }
//    public List<Comment> getAllCommentByMemberId(Long memberId){
//        return commentRepository.findAllByMemberId(memberId);
//    }
//
//    public Comment creatComment(Long postId, Long memberId, String content){
//        Comment comment = new Comment();
//        comment.setPostId(postId);
//        comment.setMemberId(memberId);
//        comment.setComment(Content);
//        return commentRepository.save(comment);
//    }
//    //댓글 수정 기능
////    코멘트 아이디로 댓글을 조회하고 댓글을 찾을 수 없으면 예외를 발생시켜 오류발생을 알림
////    수정권한 확인 조회된 댓긓의 작성자 아이디와 현재 사용자 아이디를 비교함
////    일치하지 않을 경우 RuntimeException 예외를 발생시킴
////    댓글 내용을 새 내용으로 변경합니다./
//
//    @Transactional
//    public Comment updateComment(Long commentId, memberId, String newContent){
//        Comment existingComment = commentRepository.findBy(commentId)
//                .orElseThrow(()=> new RuntimeException("댓글을 찾을 수 없습니다.다시 시도하세요"));
//        if (!existingComment.getMemberId().equals(memberId)){
//            throw new RuntimeException("댓글을 수정할 권한이 존재하지 않습니다. 다시 시도하세요");
//
//        }
//        existingComment.setComment(newContent);
//        return existingComment;
//    }
//
//    @Transactional
//    public void deleteComment(Long commentId, Long memberId) {
//        Comment existingComment = commentRepository.findById(commentId)
//                .orElseThrow(() -> new RuntimeException("Comment not found"));
//
//        // Check if the member is the owner of the comment
////        댓글 작성자가
//        if (!existingComment.getMemberId().equals(memberId)) {
//            throw new RuntimeException("Unauthorized to delete this comment");
//        }
//
//        commentRepository.delete(existingComment);
//    }
//
//    }