package com.proxyseller.twitter.repositories

import com.proxyseller.twitter.document.*
import com.proxyseller.twitter.repositories.customized.CustomizedLikeRepository
import org.springframework.data.mongodb.repository.MongoRepository

interface LikeRepository extends MongoRepository<Like, String>, CustomizedLikeRepository {

    List<Like> findByPost(Post post)

    List<Like> findByPostIn(List<Post> posts)

    Like findByPostAndUser(Post post, User user)

    void deleteByPost(Post post)

    void deleteByPostIn(List<Post> posts)
}