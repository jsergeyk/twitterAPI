package com.proxyseller.twitter.rest

import com.proxyseller.twitter.document.Comment
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.CommentDTO
import com.proxyseller.twitter.exception.PropertyNotFoundException
import com.proxyseller.twitter.repository.CommentRepository
import com.proxyseller.twitter.service.PostService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/comments")
class CommentController {

    @Autowired
    CommentRepository commentRepository
    @Autowired
    PostService postService

    @Operation(summary = "Commenting on the post")
    @PostMapping(value = "/add")
    ResponseEntity<?> addComment(@AuthenticationPrincipal User user, @RequestBody CommentDTO dto) {
        if (!dto.postId()) {
            throw new PropertyNotFoundException("postId")
        }
        def post = postService.findById(dto.postId())
        if (!post.isPresent()) {
            throw new PropertyNotFoundException(dto.postId())
        }
        def comment = new Comment(null, post.get(), user, new Date(), dto.message())
        commentRepository.save(comment)
        return ResponseEntity.ok(Map.of("id", comment.id,"postId", comment.post.id,"userId", comment.user.id,"createDate",
                comment.createDate, "message", comment.message))
    }

    @Operation(summary = "Get comments of the post")
    @GetMapping (value = "/post/{postId}")
    ResponseEntity<?> getCommentsByPost(@AuthenticationPrincipal User user, @PathVariable String postId) {
        if (!postId) {
            throw new PropertyNotFoundException("postId")
        }
        def post = postService.findById(postId)
        if (!post.isPresent()) {
            throw new PropertyNotFoundException(postId)
        }
        def comments = commentRepository.findByPost(post.get())
        def commentsDTO = new ArrayList<CommentDTO>()
        comments.forEach { comment -> commentsDTO.add(new CommentDTO(comment.id, comment.post.id, comment.user.id, comment.message, comment.createDate)) }
        return ResponseEntity.ok(commentsDTO)
    }
}
