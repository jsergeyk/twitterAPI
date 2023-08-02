package com.proxyseller.twitter.rest

import com.proxyseller.twitter.document.Like
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.LikeDTO
import com.proxyseller.twitter.repository.PostRepository
import com.proxyseller.twitter.service.LikeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/likes")
class LikeController {

    @Autowired
    LikeService likeService
    @Autowired
    PostRepository postRepository

    @PostMapping(value = "/add")
    ResponseEntity<?> addLike(@AuthenticationPrincipal User user, @RequestBody LikeDTO dto) {
        def post = likeService.findPost(dto)
        def existingLike = likeService.findByPostAndUser(post.get(), user)
        if (existingLike) {
            return ResponseEntity.badRequest().body(Map.of("description", "Like already exists"))
        } else {
            def like = new Like(null, post.get(), user, new Date())
            likeService.save(like)
            return ResponseEntity.ok(Map.of("id", like.id,"postId", like.post.id,"userId", like.user.id,"createDate",
                    like.createDate))
        }
    }

    @DeleteMapping
    ResponseEntity<?> deleteLike(@AuthenticationPrincipal User user, @RequestBody LikeDTO dto) {
        def post = likeService.findPost(dto)
        def existingLike = likeService.findByPostAndUser(post.get(), user)
        if (!existingLike) {
            return ResponseEntity.badRequest().body(Map.of("description", "Like has been deleted"))
        } else {
            likeService.delete(existingLike)
            return ResponseEntity.ok(Map.of("description", "Like deleted successfully"))
        }
    }
}
