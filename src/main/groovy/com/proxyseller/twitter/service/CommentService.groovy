package com.proxyseller.twitter.service

import com.proxyseller.twitter.document.Comment
import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.repositories.CommentRepository
import org.springframework.stereotype.Service

@Service
class CommentService {

    private CommentRepository commentRepository

    CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository
    }

    Comment save(Comment comment) {
        return commentRepository.save(comment)
    }

    void delete(Comment comment) {
        commentRepository.delete(comment)
    }

    List<Comment> findByPost(Post post) {
        return commentRepository.findByPost(post)
    }
}
