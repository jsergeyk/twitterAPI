package com.proxyseller.twitter.document

import com.mongodb.lang.NonNull
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DocumentReference

class Post {
    @Id
    String id
    @DocumentReference
    User user
    @NonNull
    Date createDate
    @NonNull
    String message

    Post() {
    }

    Post(String id, User user, @NonNull Date createDate, @NonNull String message) {
        this.id = id
        this.user = user
        this.createDate = createDate
        this.message = message
    }
}
