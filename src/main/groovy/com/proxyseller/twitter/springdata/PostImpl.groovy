package com.proxyseller.twitter.springdata

import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.repository.PostRepository
import org.springframework.stereotype.Repository

@Repository
class PostImpl implements IPost {

    private final PostRepository postRepository

    PostImpl(PostRepository postRepository) {
        this.postRepository = postRepository
    }

    @Override
    Post save(Post user) {
        return postRepository.save(user)
    }

    @Override
    Optional<Post> findById(String id) {
        return postRepository.findById(id)
    }

    @Override
    List<Post> findByUser(User user) {
        return postRepository.findByUser(user)
    }

    @Override
    List<Post> findByUser_id(String userId) {
        return postRepository.findByUser_id(userId)
    }

    @Override
    List<Post> findByUserIn(List<User> user) {
        return postRepository.findByUserIn(user)
    }

    @Override
    List<Post> deleteByUser(User user) {
        postRepository.deleteByUser(user)
    }

    @Override
    void delete(Post post) {
        postRepository.delete(post)
    }

    @Override
    boolean existsById(String id) {
        postRepository.existsById(id)
    }
}