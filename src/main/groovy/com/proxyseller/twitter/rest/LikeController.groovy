package com.proxyseller.twitter.rest

import com.proxyseller.twitter.document.Like
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.CommentDTO
import com.proxyseller.twitter.dto.LikeDTO
import com.proxyseller.twitter.exception.PropertyNotFoundException
import com.proxyseller.twitter.repository.LikeRepository
import com.proxyseller.twitter.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/likes")
class LikeController {

    @Autowired
    LikeRepository likeRepository
    @Autowired
    PostRepository postRepository

    @PostMapping(value = "/add")
    ResponseEntity<?> addComment(@AuthenticationPrincipal User user, @RequestBody LikeDTO dto) {
        def post = postRepository.findById(dto.postId())
        if (!post.isPresent()) {
            throw new PropertyNotFoundException(dto.postId())
        }
        def like = new Like(null, post.get(), user, new Date())
        likeRepository.save(like)
        return ResponseEntity.ok(Map.of("id", like.id,"postId", like.post.id,"userId", like.user.id,"createDate",
                like.createDate))
    }
}
