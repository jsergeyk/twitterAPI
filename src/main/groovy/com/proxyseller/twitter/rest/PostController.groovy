package com.proxyseller.twitter.rest

import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.service.FollowingService
import com.proxyseller.twitter.service.PostService
import com.proxyseller.twitter.service.UserService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
class PostController {

    @Autowired
    PostService postService
    @Autowired
    FollowingService followingService
    @Autowired
    UserService userService

    @Operation(summary = "Creating a post")
    @PostMapping(value = "/add")
    ResponseEntity<?> addPost(@AuthenticationPrincipal User user, @RequestBody Post post) {
        post.setUser(user)
        post.setCreateDate(new Date())
        postService.save(post)
        return ResponseEntity.ok(Map.of("id", post.id,"createDate", post.createDate, "message", post.message))
    }

    @Operation(summary = "Get the user's posts with posts of the people he followed (along with likes and comments)")
    @GetMapping
    ResponseEntity<?> getPosts(@AuthenticationPrincipal User user) {
        def posts = postService.findByUser(user)
        def followings = followingService.findByUser(user)
        def postsFollowings = postService.findByUserIn(followings.followingUser)
        posts.addAll(postsFollowings)
        def postsDTO = postService.findPostsAndCommentsAndLikes(posts)
        return ResponseEntity.ok(postsDTO)
    }

    @Operation(summary = "Get another user's posts")
    @GetMapping(value = "/user/{userId}")
    ResponseEntity<?> getPostsOtherUser(@AuthenticationPrincipal User user, @PathVariable String userId) {
        if (!userId || !userService.findById(userId)) {
            return ResponseEntity.badRequest().body(Map.of("description", "User cannot be self-followed"))
        }
        def posts = postService.findByUser_id(userId)
        def postsDTO = postService.findPostsAndCommentsAndLikes(posts)
        return ResponseEntity.ok(postsDTO)
    }

    @Operation(summary = "Editing a post")
    @PatchMapping(value = "/edit/{id}")
    ResponseEntity<?> editPost(@AuthenticationPrincipal User user, @PathVariable String id, @RequestBody Post post) {
        Post existingPost = postService.findById(id).orElseThrow()
        if (existingPost.user == user) {
            BeanUtils.copyProperties(post, existingPost, "id", "createDate", "user")
            postService.save(existingPost)
        } else {
            throw new AccessDeniedException("Access denied")
        }
        return ResponseEntity.ok(Map.of("id", existingPost.id,"createDate", existingPost.createDate, "message", existingPost.message))
    }

    @Operation(summary = "Deleting a post")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Map<String, String>> deletePost(@AuthenticationPrincipal User user, @PathVariable String id) {
        def post = postService.findById(id)
        if (post.isPresent()) {
            if (post.get().user == user) {
                postService.delete(post.get())
                return ResponseEntity.ok(Map.of("description", "Post successfully deleted"))
            } else {
                throw new AccessDeniedException("Access denied")
            }
        }
        return ResponseEntity.notFound().build()
    }
}
