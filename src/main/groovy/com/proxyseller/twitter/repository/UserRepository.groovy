package com.proxyseller.twitter.repository

import com.proxyseller.twitter.document.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username)

    Optional<User> findByEmail(String email)

    void deleteByUsername(String username)
}