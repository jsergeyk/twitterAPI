package com.proxyseller.twitter.rest

import com.proxyseller.twitter.document.Like
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.LikeDTO
import com.proxyseller.twitter.service.LikeService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/likes")
class LikeController {

    @Autowired
    LikeService likeService

    @Operation(summary = "Add like")
    @PostMapping
    ResponseEntity<?> addLike(@AuthenticationPrincipal User user, @RequestBody LikeDTO dto) {
        def post = likeService.findPost(dto)
        def existingLike = likeService.findByPostAndUser(post.get(), user)
        if (existingLike) {
            return ResponseEntity.badRequest().body(Map.of("description", "Like already exist"))
        } else {
            def like = new Like(null, post.get(), user, new Date())
            likeService.save(like)
            return ResponseEntity.ok(new LikeDTO(like.id, like.post.id, like.user.id, like.createDate))
        }
    }

    @Operation(summary = "Delete like")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteLike(@AuthenticationPrincipal User user,  @PathVariable String id) {
        def existingLike = likeService.findById(id)
        if (!existingLike.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("description", "Like does not exist"))
        }
        if (existingLike.get().user != user) {
            throw new AccessDeniedException("Access denied")
        }
        likeService.delete(existingLike.get())
        return ResponseEntity.ok(Map.of("description", "Like deleted successfully"))
    }
}
