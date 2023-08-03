package com.proxyseller.twitter.rest

import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.CommentDTO
import com.proxyseller.twitter.dto.LikeDTO
import com.proxyseller.twitter.dto.PostDTO
import com.proxyseller.twitter.repository.CommentRepository
import com.proxyseller.twitter.repository.LikeRepository
import com.proxyseller.twitter.repository.PostRepository
import com.proxyseller.twitter.service.FollowingService
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
    LikeRepository likeRepository
    @Autowired
    FollowingService followingService

    @PostMapping(value = "/add")
    ResponseEntity<?> addPost(@AuthenticationPrincipal User user, @RequestBody Post post) {
        post.setUser(user)
        post.setCreateDate(new Date())
        postRepository.save(post)
        return ResponseEntity.ok(Map.of("id", post.id,"createDate", post.createDate, "message", post.message))
    }

    @GetMapping
    ResponseEntity<?> getUserPosts(@AuthenticationPrincipal User user) {
        def posts = postRepository.findByUser(user)
        def followings = followingService.findByUser(user)
        def postsFollowings = postRepository.findByUserIn(followings.followingUser)
        posts.addAll(postsFollowings)
        def response = new ArrayList<PostDTO>()
        def comments = commentRepository.findByPostIn(posts)
        def likes = likeRepository.findByPostIn(posts)
        posts.toSet().forEach {post ->{
            def commentsByPost = comments.findAll { comment -> comment.post == post }
                    .collect {comment -> return new CommentDTO(comment.id, comment.post.id, comment.user.id, comment.message, comment.createDate)}
            def likesByPost = likes.findAll { like -> like.post == post }
                    .collect {like -> return new LikeDTO(like.id, like.post.id, like.user.id, like.createDate)}
            def postDTO = new PostDTO(post.id, post.user.id, post.message, post.createDate, commentsByPost, likesByPost)
            response.add(postDTO)
        }}
        return ResponseEntity.ok(response)
    }

    @PatchMapping(value = "/edit/{id}")
    ResponseEntity<?> editPost(@AuthenticationPrincipal User user, @PathVariable String id, @RequestBody Post post) {
        Post existingPost = postRepository.findById(id).orElseThrow()
        if (existingPost.user == user) {
            BeanUtils.copyProperties(post, existingPost, "id", "createDate", "user")
            postRepository.save(existingPost)
        } else {
            throw new AccessDeniedException("Access denied")
        }
        return ResponseEntity.ok(Map.of("id", existingPost.id,"createDate", existingPost.createDate, "message", existingPost.message))
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<Map<String, String>> deletePost(@AuthenticationPrincipal User user, @PathVariable String id) {
        def post = postRepository.findById(id)
        if (post.isPresent()) {
            if (post.get().user == user) {
                postRepository.deleteById(id)
                return ResponseEntity.ok(Map.of("description", "Post successfully deleted"))
            } else {
                throw new AccessDeniedException("Access denied")
            }
        }
        return ResponseEntity.notFound().build()
    }
}
