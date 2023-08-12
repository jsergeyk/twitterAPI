package com.proxyseller.twitter.service

import com.proxyseller.twitter.document.Comment
import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.springdata.IComment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentService {

    @Autowired
    private IComment commentRepository

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
