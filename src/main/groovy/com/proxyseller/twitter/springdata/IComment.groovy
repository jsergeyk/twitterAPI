package com.proxyseller.twitter.springdata

import com.proxyseller.twitter.document.Comment
import com.proxyseller.twitter.document.Post

interface IComment {

        Optional<Comment> findById(String id)

        Comment save(Comment post);

        List<Comment> findByPost(Post post)

        List<Comment> findByPostIn(List<Post> posts)

        void delete(Comment comment)
}