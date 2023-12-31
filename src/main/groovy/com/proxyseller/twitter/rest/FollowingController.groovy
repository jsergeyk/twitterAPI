package com.proxyseller.twitter.rest

import com.proxyseller.twitter.document.Following
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.FollowingDTO
import com.proxyseller.twitter.service.FollowingService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/following")
class FollowingController {

    private FollowingService followingService

    FollowingController(FollowingService followingService) {
        this.followingService = followingService
    }

    @Operation(summary = "Following on the user")
    @PostMapping
    ResponseEntity<?> addFollowing(@AuthenticationPrincipal User user, @RequestBody FollowingDTO dto) {
        if (user.id == dto.followingUserId()) {
            return ResponseEntity.badRequest().body(Map.of("description", "User cannot be self-followed"))
        }
        def followingUser = followingService.findFollowingUser(dto)
        def followings = followingService.findByUser(user)
        if (followings*.followingUser.contains(followingUser.get())) {
            return ResponseEntity.badRequest().body(Map.of("description", "The user has been followed"))
        } else {
            def following = new Following(null, user, followingUser.get())
            followingService.save(following)
            return ResponseEntity.ok(new FollowingDTO(user.id, following.followingUser.id))
        }
    }

    @Operation(summary = "Unfollowing on the user")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteFollowing(@AuthenticationPrincipal User user, @PathVariable String id) {
        def followings = followingService.findByUser(user)
        if (followings*.followingUser*.id.contains(id)) {
            followingService.delete(followings.findAll{it.followingUser.id == id}.first())
            return ResponseEntity.ok(Map.of("description", "Unfollowing success"))
        } else {
            return ResponseEntity.badRequest().body(Map.of("description", "The user has not been followed"))
        }
    }
}
