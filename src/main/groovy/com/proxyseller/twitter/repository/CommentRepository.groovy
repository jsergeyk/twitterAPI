package com.proxyseller.twitter.repository

import com.proxyseller.twitter.document.Comment
import com.proxyseller.twitter.document.Post
import org.springframework.data.mongodb.repository.MongoRepository

interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByPost(Post post)

    List<Comment> findByPostIn(List<Post> posts)

    void deleteByPost(Post post)

    void deleteByPostIn(List<Post> posts)
}