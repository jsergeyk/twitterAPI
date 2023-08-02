package com.proxyseller.twitter.rest

import com.proxyseller.twitter.document.Following
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.FollowingDTO
import com.proxyseller.twitter.service.FollowingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/following")
class FollowingController {

    @Autowired
    FollowingService followingService

    @PostMapping(value = "/add")
    ResponseEntity<?> addFollowing(@AuthenticationPrincipal User user, @RequestBody FollowingDTO dto) {
        def followingUser = followingService.findFollowingUser(dto)
        def followings = followingService.findByUser(user)
        if (followings*.followingUser.contains(followingUser.get())) {
            return ResponseEntity.badRequest().body(Map.of("description", "The user has been followed"))
        } else {
            def following = new Following(null, user, followingUser.get())
            followingService.save(following)
            return ResponseEntity.ok(Map.of("id", following.id,"userId", user.id,"followingUser", following.followingUser.id))
        }
    }

    @DeleteMapping
    ResponseEntity<?> deleteFollowing(@AuthenticationPrincipal User user, @RequestBody FollowingDTO dto) {
        def followingUser = followingService.findFollowingUser(dto)
        def followings = followingService.findByUser(user)
        if (followings*.followingUser.contains(followingUser.get())) {
            followingService.delete(followings.findAll{it.followingUser == followingUser.get()}.first())
            return ResponseEntity.ok(Map.of("description", "Unfollowing success"))
        } else {
            return ResponseEntity.badRequest().body(Map.of("description", "The user has not been followed"))
        }
    }
}
