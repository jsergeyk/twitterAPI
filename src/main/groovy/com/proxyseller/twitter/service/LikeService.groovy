package com.proxyseller.twitter.service

import com.proxyseller.twitter.document.Like
import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.LikeDTO
import com.proxyseller.twitter.exception.PropertyNotFoundException
import com.proxyseller.twitter.repository.LikeRepository
import com.proxyseller.twitter.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LikeService {

    @Autowired
    private LikeRepository likeRepository
    @Autowired
    private PostRepository postRepository

    Like save(Like following) {
        return likeRepository.save(following)
    }

    void delete(Like following) {
        likeRepository.delete(following)
    }

    Like findByPostAndUser(Post post, User user) {
        return likeRepository.findByPostAndUser(post, user)
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
