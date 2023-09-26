package com.proxyseller.twitter.rest

import com.proxyseller.twitter.document.Comment
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.CommentDTO
import com.proxyseller.twitter.exception.PropertyNotFoundException
import com.proxyseller.twitter.service.CommentService
import com.proxyseller.twitter.service.PostService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/comments")
class CommentController {

    private CommentService commentService
    private PostService postService

    CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService
        this.postService = postService
    }

    @Operation(summary = "Commenting on the post")
    @PostMapping
    ResponseEntity<?> addComment(@AuthenticationPrincipal User user, @RequestBody CommentDTO dto) {
        if (!dto.postId()) {
            throw new PropertyNotFoundException("postId")
        }
        def post = postService.findById(dto.postId())
        if (!post.isPresent()) {
            throw new PropertyNotFoundException(dto.postId())
        }
        def comment = new Comment(null, post.get(), user, new Date(), dto.message())
        commentService.save(comment)
        return ResponseEntity.ok(new CommentDTO(comment.id, comment.post.id, comment.user.id, comment.message, comment.createDate))
    }
}
