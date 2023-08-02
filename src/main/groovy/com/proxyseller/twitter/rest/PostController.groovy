package com.proxyseller.twitter.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.CommentDTO
import com.proxyseller.twitter.dto.PostDTO
import com.proxyseller.twitter.repository.CommentRepository
import com.proxyseller.twitter.repository.PostRepository
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
    PostRepository postRepository
    @Autowired
    CommentRepository commentRepository
    @Autowired
    ObjectMapper objectMapper

    @PostMapping(value = "/add")
    ResponseEntity<?> addPost(@AuthenticationPrincipal User user, @RequestBody Post post) {
        post.setUser(user)
        post.setCreateDate(new Date())
        postRepository.save(post)
        return ResponseEntity.ok(Map.of("id", post.getId(),"createDate", post.getCreateDate(), "message", post.getMessage()))
    }

    @GetMapping
    ResponseEntity<?> currentUserName(@AuthenticationPrincipal User user) {
        def posts = postRepository.findByUser(user)
        def response = new ArrayList<PostDTO>()
        def allComments = commentRepository.findByPostIn(posts)
        allComments*.getPost().toSet().forEach {post ->{
            def commentsByPost = allComments.findAll { comment -> comment.post == post }
                    .collect {comment -> return new CommentDTO(comment.id, comment.post.id, comment.user.id, comment.message, comment.createDate)}
            def postDTO = new PostDTO(post.id, post.user.id, post.message, post.createDate, commentsByPost)
            response.add(postDTO)
        }}
        return ResponseEntity.ok(response)
    }

    @PatchMapping(value = "/edit/{id}")
    ResponseEntity<?> editPost(@AuthenticationPrincipal User user, @PathVariable String id, @RequestBody Post post) {
        Post existingPost = postRepository.findById(id).orElseThrow()
        if (existingPost.getUser() == user) {
            BeanUtils.copyProperties(post, existingPost, "id", "createDate", "user")
            postRepository.save(existingPost)
        } else {
            throw new AccessDeniedException("Access denied")
        }
        return ResponseEntity.ok(Map.of("id", existingPost.getId(),"createDate", existingPost.getCreateDate(), "message", existingPost.getMessage()))
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<Map<String, String>> deletePost(@AuthenticationPrincipal User user, @PathVariable String id) {
        def post = postRepository.findById(id)
        if (post.isPresent()) {
            if (post.get().getUser() == user) {
                postRepository.deleteById(id)
                return ResponseEntity.ok(Map.of("description", "Post successfully deleted"))
            } else {
                throw new AccessDeniedException("Access denied")
            }
        }
        return ResponseEntity.notFound().build()
    }
}
