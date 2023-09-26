package com.proxyseller.twitter.rest

import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.CommentDTO
import com.proxyseller.twitter.dto.PostDTO
import com.proxyseller.twitter.exception.PropertyNotFoundException
import com.proxyseller.twitter.service.CommentService
import com.proxyseller.twitter.service.FollowingService
import com.proxyseller.twitter.service.PostService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.BeanUtils
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
class PostController {

    private PostService postService
    private CommentService commentService
    private FollowingService followingService

    PostController(PostService postService, CommentService commentService, FollowingService followingService) {
        this.postService = postService
        this.commentService = commentService
        this.followingService = followingService
    }

    @Operation(summary = "Creating a post")
    @PostMapping
    ResponseEntity<?> addPost(@AuthenticationPrincipal User user, @RequestBody Post post) {
        post.setUser(user)
        post.setCreateDate(new Date())
        postService.save(post)
        return ResponseEntity.ok(new PostDTO(post.id, post.user.id, post.message, post.createDate, null, null))
    }

    @Operation(summary = "Get the current user's posts with posts of the people he followed (along with likes and comments)")
    @GetMapping
    ResponseEntity<?> getPosts(@AuthenticationPrincipal User user) {
        def posts = postService.findByUser(user)
        def followings = followingService.findByUser(user)
        def postsFollowings = postService.findByUserIn(followings.followingUser)
        posts.addAll(postsFollowings)
        def postsDTO = postService.findPostsAndCommentsAndLikes(posts)
        return ResponseEntity.ok(postsDTO)
    }

    @Operation(summary = "Get comments of the post")
    @GetMapping (value = "/{id}/comments")
    ResponseEntity<?> getCommentsByPost(@AuthenticationPrincipal User user, @PathVariable String id) {
        if (!id) {
            throw new PropertyNotFoundException("id")
        }
        def post = postService.findById(id)
        if (!post.isPresent()) {
            throw new PropertyNotFoundException(id)
        }
        def comments = commentService.findByPost(post.get())
        def commentsDTO = new ArrayList<CommentDTO>()
        comments.forEach { comment -> commentsDTO.add(new CommentDTO(comment.id, comment.post.id, comment.user.id, comment.message, comment.createDate)) }
        return ResponseEntity.ok(commentsDTO.sort {commentDTO -> commentDTO.createDate()})
    }

    @Operation(summary = "Editing a post")
    @PutMapping(value = "/{id}")
    ResponseEntity<?> editPostPut(@AuthenticationPrincipal User user, @PathVariable String id, @RequestBody Post postDto) {
        Post existingPost = postService.findById(id).orElseThrow()
        if (existingPost.user == user) {
            BeanUtils.copyProperties(postDto, existingPost, "id", "createDate", "user")
            postService.save(existingPost)
        } else {
            throw new AccessDeniedException("Access denied")
        }
        def postDTO = postService.findPostAndCommentsAndLikes(existingPost)
        return ResponseEntity.ok(postDTO)
    }

    @Operation(summary = "Editing a post")
    @PatchMapping(value = "/{id}")
    ResponseEntity<?> editPost(@AuthenticationPrincipal User user, @PathVariable String id, @RequestBody Post postDto) {
        Post existingPost = postService.findById(id).orElseThrow()
        if (existingPost.user == user) {
            existingPost.message = postDto.getMessage()
            postService.save(existingPost)
        } else {
            throw new AccessDeniedException("Access denied")
        }
        def postDTO = postService.findPostAndCommentsAndLikes(existingPost)
        return ResponseEntity.ok(postDTO)
    }

    @Operation(summary = "Deleting a post")
    @DeleteMapping("/{id}")
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
