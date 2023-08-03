package com.proxyseller.twitter.repository

import com.proxyseller.twitter.document.Following
import com.proxyseller.twitter.document.User
import org.springframework.data.mongodb.repository.MongoRepository

interface FollowingRepository extends MongoRepository<Following, String> {

    List<Following> findByUser(User user)
}