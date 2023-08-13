package com.proxyseller.twitter.springdata

import com.proxyseller.twitter.document.Like
import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.repository.LikeRepository
import org.springframework.stereotype.Repository

@Repository
class LikeImpl implements ILike {

    private final LikeRepository likeRepository

    LikeImpl(LikeRepository likeRepository) {
        this.likeRepository = likeRepository
    }

    @Override
    Like save(Like like) {
        return likeRepository.save(like)
    }

    @Override
    Optional<Like> findById(String id) {
        return likeRepository.findById(id);
    }

    @Override
    void delete(Like like) {
        likeRepository.delete(like)
    }

    @Override
    List<Like> findByPost(Post post) {
        return likeRepository.findByPost(post)
    }


    @Override
    List<Like> findByPostIn(List<Post> posts) {
        return likeRepository.findByPostIn(posts)
    }

    @Override
    Like findByPostAndUser(Post post, User user) {
        return likeRepository.findByPostAndUser(post, user)
    }

    @Override
    void deleteByPost(Post post) {
        likeRepository.deleteByPost(post)
    }

    @Override
    void deleteByPostIn(List<Post> posts){
        likeRepository.deleteByPostIn(posts)
    }
}