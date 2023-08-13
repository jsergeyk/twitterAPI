package com.proxyseller.twitter.springdata

import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.document.User

interface IPost {

        Optional<Post> findById(String id)

        Post save(Post post);

        List<Post> findByUser(User user)

        List<Post> findByUser_id(String userId)

        List<Post> findByUserIn(List<User> user)

        void delete(Post post)

        List<Post> deleteByUser(User user)

        boolean existsById(String id)
}