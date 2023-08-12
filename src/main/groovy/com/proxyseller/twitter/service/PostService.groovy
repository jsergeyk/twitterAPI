package com.proxyseller.twitter.service

import com.proxyseller.twitter.document.Post
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.CommentDTO
import com.proxyseller.twitter.dto.LikeDTO
import com.proxyseller.twitter.dto.PostDTO
import com.proxyseller.twitter.springdata.IComment
import com.proxyseller.twitter.springdata.ILike
import com.proxyseller.twitter.springdata.IPost
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PostService {

    @Autowired
    private IPost postRepository
    @Autowired
    private IComment commentRepository
    @Autowired
    private ILike likeRepository


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
    }

    void deleteByUser(User user) {
        postRepository.deleteByUser(user)
    }

    ArrayList<PostDTO> findPostsAndCommentsAndLikes(posts) {
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
        return response
    }
}
