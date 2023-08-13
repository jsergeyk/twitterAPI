package com.proxyseller.twitter.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import javax.validation.constraints.NotNull

@Document
class RefreshToken {
    @Id
    String id
    @NotNull
    @DocumentReference
    User user

    RefreshToken() {
    }

    RefreshToken(String id, User user) {
        this.id = id
        this.user = user
    }
}
