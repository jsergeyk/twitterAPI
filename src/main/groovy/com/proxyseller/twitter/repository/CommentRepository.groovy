package com.proxyseller.twitter.repository

import com.proxyseller.twitter.document.Comment
import org.springframework.data.mongodb.repository.MongoRepository

interface CommentRepository extends MongoRepository<Comment, String> {

}