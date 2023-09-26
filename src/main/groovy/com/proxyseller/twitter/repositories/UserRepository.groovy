package com.proxyseller.twitter.repositories

import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.repositories.customized.CustomizedUserRepository
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository extends MongoRepository<User, String>, CustomizedUserRepository {

    Optional<User> findByUsername(String username)

    Optional<User> findByEmail(String email)

    void deleteByUsername(String username)
}