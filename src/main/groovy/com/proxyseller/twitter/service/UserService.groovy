package com.proxyseller.twitter.service

import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.repositories.RefreshTokenRepository
import com.proxyseller.twitter.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService implements UserDetailsService {

    private UserRepository userRepository
    private RefreshTokenRepository refreshTokenRepository
    private PostService postService
    private FollowingService followingService

    UserService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, PostService postService, FollowingService followingService) {
        this.userRepository = userRepository
        this.refreshTokenRepository = refreshTokenRepository
        this.postService = postService
        this.followingService = followingService
    }

    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"))
    }

    User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"))
    }

    Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
    }

    Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
    }

    User save(User user) {
        return userRepository.save(user)
    }

    void delete(User user) {
        refreshTokenRepository.deleteByUser(user)
        postService.deleteByUser(user)
        followingService.deleteByUser(user)
        followingService.deleteByFollowingUser(user)
        userRepository.delete(user)
    }

    void deleteByUsername(String username) {
        userRepository.deleteByUsername(username)
    }

    boolean existsById(String id) {
        userRepository.existsById(id)
    }
}
