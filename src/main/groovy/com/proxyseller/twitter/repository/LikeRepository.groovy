package com.proxyseller.twitter.repository

import com.proxyseller.twitter.document.*
import org.springframework.data.mongodb.repository.MongoRepository

interface LikeRepository extends MongoRepository<Like, String> {

    List<Comment> findByPost(Post post);

    List<Comment> findByPostIn(List<Post> posts);
}