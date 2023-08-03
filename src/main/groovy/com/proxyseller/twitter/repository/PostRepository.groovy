package com.proxyseller.twitter.repository

import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.document.User
import org.springframework.data.mongodb.repository.MongoRepository

interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findByUser(User user);

    List<Post> findByUserIn(List<User> user);

    void deleteByUser(User user);

}