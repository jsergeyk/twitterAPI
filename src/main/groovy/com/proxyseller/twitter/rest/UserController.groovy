package com.proxyseller.twitter.rest

import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.SignupDTO
import com.proxyseller.twitter.repository.PostRepository
import com.proxyseller.twitter.repository.RefreshTokenRepository
import com.proxyseller.twitter.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController {

    @Autowired
    private UserRepository userRepository
    @Autowired
    private PostRepository postRepository
    @Autowired
    private RefreshTokenRepository refreshTokenRepository
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/me")
    ResponseEntity<?> currentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of("username", user.getUsername(),"email", user.getEmail()))
    }

    @PatchMapping(value = "/edit/{id}")
    @PreAuthorize("#authUser.id == #id")
    ResponseEntity<?> editPost(@AuthenticationPrincipal User authUser, @PathVariable String id, @RequestBody SignupDTO user) {
        authUser.email = user.email() ?: authUser.email
        authUser.username = user.username() ?: authUser.username
        authUser.isActive = user.isActive() ?: authUser.isActive
        if (user.password() != null) {
            authUser.setPassword(passwordEncoder.encode(user.password()));
        }
        userRepository.save(authUser)
        return ResponseEntity.ok(authUser)
    }

    @DeleteMapping("/delete")
    //@Transactional
    ResponseEntity<Map<String, String>> deleteUser(@AuthenticationPrincipal User user) {
        postRepository.deleteByUser(user)
        refreshTokenRepository.deleteByUser(user)
        userRepository.delete(user)
        return ResponseEntity.ok(Map.of("description", "User successfully deleted"))
    }
}
