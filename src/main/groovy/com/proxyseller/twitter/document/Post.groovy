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

    @Override
    boolean equals(o) {
        if (this.is(o)) return true
        if (o == null || getClass() != o.class) return false

        Post post = (Post) o

        if (createDate != post.createDate) return false
        if (id != post.id) return false
        if (message != post.message) return false
        if (user != post.user) return false

        return true
    }

    @Override
    int hashCode() {
        int result
        result = id.hashCode()
        result = 31 * result + user.hashCode()
        result = 31 * result + createDate.hashCode()
        result = 31 * result + message.hashCode()
        return result
    }
}
