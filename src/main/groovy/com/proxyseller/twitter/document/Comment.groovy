package com.proxyseller.twitter.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import javax.validation.constraints.NotNull

@Document
class Comment {
    @Id
    String id
    @DocumentReference
    @NotNull
    Post post
    @NotNull
    @DocumentReference
    User user
    @NotNull
    Date createDate
    @NotNull
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

    @Override
    boolean equals(o) {
        if (this.is(o)) return true
        if (o == null || getClass() != o.class) return false

        Comment comment = (Comment) o

        if (createDate != comment.createDate) return false
        if (id != comment.id) return false
        if (message != comment.message) return false
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
        result = 31 * result + message.hashCode()
        return result
    }
}
