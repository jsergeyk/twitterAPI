package com.proxyseller.twitter.springdata

import com.proxyseller.twitter.document.Like
import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.document.User

interface ILike {

        Optional<Like> findById(String id)

        Like save(Like user)

        void delete(Like like)

        List<Like> findByPost(Post post)

        List<Like> findByPostIn(List<Post> posts)

        Like findByPostAndUser(Post post, User user)
}