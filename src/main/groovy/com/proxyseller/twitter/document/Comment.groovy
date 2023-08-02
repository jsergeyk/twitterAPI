package com.proxyseller.twitter.document

import com.mongodb.lang.NonNull
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DocumentReference

class Comment {
    @Id
    String id
    @DocumentReference
    @NonNull
    Post post
    @NonNull
    @DocumentReference
    User user
    @NonNull
    Date createDate
    @NonNull
    String message

    Comment() {
    }

    Comment(String id, Post post, User user, Date createDate, String message) {
        this.id = id
        this.post = post
        this.user = user
        this.createDate = createDate
        this.message = message
    }
}
