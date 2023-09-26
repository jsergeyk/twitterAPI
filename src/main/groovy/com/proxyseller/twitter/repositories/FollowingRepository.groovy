package com.proxyseller.twitter.repositories

import com.proxyseller.twitter.document.Following
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.repositories.customized.CustomizedFollowingRepository
import org.springframework.data.mongodb.repository.MongoRepository

interface FollowingRepository extends MongoRepository<Following, String>, CustomizedFollowingRepository {

    List<Following> findByUser(User user)

    void deleteByUser(User user)

    void deleteByFollowingUser(User user)
}