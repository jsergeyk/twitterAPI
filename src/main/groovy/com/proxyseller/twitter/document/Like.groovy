package com.proxyseller.twitter.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import javax.validation.constraints.NotNull


@Document
@CompoundIndex(name = "post_user_idx", def = "{'post.id': 1, 'user.id': 1}", unique = true)
class Like {
    @Id
    String id
    @NotNull
    @DocumentReference
    Post post
    @NotNull
    @DocumentReference
    User user
    @NotNull
    Date createDate

    Like() {
    }

    Like(String id, Post post, User user, Date createDate) {
        this.id = id
        this.post = post
        this.user = user
        this.createDate = createDate
    }

    @Override
    boolean equals(o) {
        if (this.is(o)) return true
        if (o == null || getClass() != o.class) return false
        Like comment = (Like) o
        if (createDate != comment.createDate) return false
        if (id != comment.id) return false
        if (post != comment.post) return false
        if (user != comment.user) return false
        return true
    }

    @Override
    int hashCode() {
        int result
        result = id.hashCode()
        result = 31 * result + post.hashCode()
        result = 31 * result + user.hashCode()
        result = 31 * result + createDate.hashCode()
        return result
    }
}
