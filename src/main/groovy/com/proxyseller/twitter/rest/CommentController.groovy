package com.proxyseller.twitter.rest

import com.proxyseller.twitter.document.Comment
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.CommentDTO
import com.proxyseller.twitter.exception.PropertyNotFoundException
import com.proxyseller.twitter.repository.CommentRepository
import com.proxyseller.twitter.repository.PostRepository
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
    PostRepository postRepository

    @PostMapping(value = "/add")
    ResponseEntity<?> addComment(@AuthenticationPrincipal User user, @RequestBody CommentDTO dto) {
        def post = postRepository.findById(dto.postId())
        if (!post.isPresent()) {
            throw new PropertyNotFoundException(dto.postId())
        }
        def comment = new Comment(null, post.get(), user, new Date(), dto.message())
        commentRepository.save(comment)
        return ResponseEntity.ok(Map.of("id", comment.getId(),"postId", dto.postId(),"userId", comment.getUser().getId(),"createDate",
                comment.getCreateDate(), "message", comment.getMessage()))
    }
}