package com.proxyseller.twitter.repository

import com.proxyseller.twitter.document.*
import org.springframework.data.mongodb.repository.MongoRepository

interface LikeRepository extends MongoRepository<Like, String> {

    List<Like> findByPost(Post post)

    List<Like> findByPostIn(List<Post> posts)

    Like findByPostAndUser(Post post, User user)

    void deleteByPost(Post post)

    void deleteByPostIn(List<Post> posts)
}