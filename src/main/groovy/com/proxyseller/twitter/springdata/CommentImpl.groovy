package com.proxyseller.twitter.springdata

import com.proxyseller.twitter.document.Comment
import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.repository.CommentRepository
import org.springframework.stereotype.Repository

@Repository
class CommentImpl implements IComment {

    private final CommentRepository commentRepository

    CommentImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository
    }

    @Override
    Comment save(Comment comment) {
        return commentRepository.save(comment)
    }

    @Override
    Optional<Comment> findById(String id) {
        return commentRepository.findById(id)
    }

    @Override
    List<Comment> findByPost(Post post) {
        return commentRepository.findByPost(post)
    }

    @Override
    List<Comment> findByPostIn(List<Post> posts) {
        return commentRepository.findByPostIn(posts)
    }

    @Override
    void delete(Comment comment) {
        commentRepository.delete(comment)
    }
}