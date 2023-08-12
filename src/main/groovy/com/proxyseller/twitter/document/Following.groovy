package com.proxyseller.twitter.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import javax.validation.constraints.NotNull

@Document
@CompoundIndex(name = "user_followingUser_idx", def = "{'user.id': 1, 'followingUser.id': 1}", unique = true)
class Following {

    @Id
    String id
    @NotNull
    @DocumentReference
    User user
    @NotNull
    @DocumentReference
    User followingUser

    Following(String id, User user, User followingUser) {
        this.id = id
        this.user = user
        this.followingUser = followingUser
    }
}
