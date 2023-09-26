package com.proxyseller.twitter.repositories

import com.proxyseller.twitter.document.RefreshToken
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.repositories.customized.CustomizedRefreshTokenRepository
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository extends MongoRepository<RefreshToken, String>, CustomizedRefreshTokenRepository {

    void deleteByUser(User user)

}