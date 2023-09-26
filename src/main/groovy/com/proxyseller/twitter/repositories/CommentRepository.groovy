package com.proxyseller.twitter.repositories

import com.proxyseller.twitter.document.Comment
import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.repositories.customized.CustomizedCommentRepository
import org.springframework.data.mongodb.repository.MongoRepository

interface CommentRepository extends MongoRepository<Comment, String>, CustomizedCommentRepository {

    List<Comment> findByPost(Post post)

    List<Comment> findByPostIn(List<Post> posts)

    void deleteByPost(Post post)

    void deleteByPostIn(List<Post> posts)
}