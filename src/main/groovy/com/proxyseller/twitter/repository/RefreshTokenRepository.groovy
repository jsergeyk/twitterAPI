package com.proxyseller.twitter.repository

import com.proxyseller.twitter.document.RefreshToken
import com.proxyseller.twitter.document.User
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

    void deleteByUser(User user)

}