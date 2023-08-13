package com.proxyseller.twitter.service

import com.proxyseller.twitter.document.Like
import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.LikeDTO
import com.proxyseller.twitter.exception.PropertyNotFoundException
import com.proxyseller.twitter.springdata.ILike
import com.proxyseller.twitter.springdata.IPost
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LikeService {

    @Autowired
    private ILike likeRepository
    @Autowired
    private IPost postRepository

    Like save(Like like) {
        return likeRepository.save(like)
    }

    void delete(Like like) {
        likeRepository.delete(like)
    }

    Like findByPostAndUser(Post post, User user) {
        return likeRepository.findByPostAndUser(post, user)
    }

    List<Like> findByPost(Post post) {
        return likeRepository.findByPost(post)
    }

    Optional<Post> findPost(LikeDTO dto) {
        if (!dto.postId()) {
            throw new PropertyNotFoundException(dto.postId())
        }
        def post = postRepository.findById(dto.postId())
        if (!post.isPresent()) {
            throw new PropertyNotFoundException(dto.postId())
        }
        return post
    }
}
