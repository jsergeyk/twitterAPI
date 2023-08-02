package com.proxyseller.twitter.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document
class RefreshToken {
    @Id
    String id
    @DocumentReference
    User user

    RefreshToken() {
    }

    RefreshToken(String id, User user) {
        this.id = id
        this.user = user
    }
}
