package com.proxyseller.twitter.service

import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.CommentDTO
import com.proxyseller.twitter.dto.LikeDTO
import com.proxyseller.twitter.dto.PostDTO
import com.proxyseller.twitter.repositories.CommentRepository
import com.proxyseller.twitter.repositories.LikeRepository
import com.proxyseller.twitter.repositories.PostRepository
import org.springframework.stereotype.Service

@Service
class PostService {

    private PostRepository postRepository
    private CommentRepository commentRepository
    private LikeRepository likeRepository

    PostService(PostRepository postRepository, CommentRepository commentRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository
        this.commentRepository = commentRepository
        this.likeRepository = likeRepository
    }

    Post save(Post post) {
        return postRepository.save(post)
    }

    Optional<Post> findById(String id) {
        postRepository.findById(id)
    }

    List<Post> findByUser(User user) {
        postRepository.findByUser(user)
    }

    List<Post> findByUser_id(String userId) {
        postRepository.findByUser_id(userId)
    }

    List<Post> findByUserIn(List<User> users) {
        postRepository.findByUserIn(users)
    }

    void delete(Post post) {
        postRepository.delete(post)
        commentRepository.deleteByPost(post)
        likeRepository.deleteByPost(post)
    }

    void deleteByUser(User user) {
        List<Post> posts = postRepository.deleteByUser(user)
        commentRepository.deleteByPostIn(posts)
        likeRepository.deleteByPostIn(posts)
    }

    boolean existsById(String id) {
        postRepository.existsById(id)
    }

    ArrayList<PostDTO> findPostsAndCommentsAndLikes(List<Post> posts) {
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
        return response.sort {post1,post2 -> post2.createDate() <=> post1.createDate()}
    }

    PostDTO findPostAndCommentsAndLikes(Post post) {
        def comments = commentRepository.findByPost(post)
        def likes = likeRepository.findByPost(post)
        def commentsByPost = comments.collect {comment -> return new CommentDTO(comment.id, comment.post.id, comment.user.id, comment.message, comment.createDate)}
        def likesByPost = likes.collect {like -> return new LikeDTO(like.id, like.post.id, like.user.id, like.createDate)}
        return new PostDTO(post.id, post.user.id, post.message, post.createDate, commentsByPost, likesByPost)
    }
}
