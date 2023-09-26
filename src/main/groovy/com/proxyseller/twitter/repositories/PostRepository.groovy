package com.proxyseller.twitter.repositories

import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.repositories.customized.CustomizedPostRepository
import org.springframework.data.mongodb.repository.MongoRepository

interface PostRepository extends MongoRepository<Post, String>, CustomizedPostRepository {

    List<Post> findByUser(User user)

    List<Post> findByUser_id(String userId)

    List<Post> findByUserIn(List<User> user)

    List<Post> deleteByUser(User user)

}