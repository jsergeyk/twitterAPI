package com.proxyseller.twitter.rest

import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.UserDTO
import com.proxyseller.twitter.repository.PostRepository
import com.proxyseller.twitter.repository.RefreshTokenRepository
import com.proxyseller.twitter.service.UserService
import io.swagger.v3.oas.annotations.Operation
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
    private UserService userService
    @Autowired
    private PostRepository postRepository
    @Autowired
    private RefreshTokenRepository refreshTokenRepository
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Editing a user")
    @PatchMapping(value = "/edit/{id}")
    @PreAuthorize("#authUser.id == #id")
    ResponseEntity<?> editPost(@AuthenticationPrincipal User authUser, @PathVariable String id, @RequestBody UserDTO userDto) {
        authUser.email = userDto.email() ?: authUser.email
        authUser.username = userDto.username() ?: authUser.username
        authUser.isActive = userDto.isActive() ?: authUser.isActive
        if (userDto.password() != null) {
            authUser.setPassword(passwordEncoder.encode(userDto.password()))
        }
        userService.save(authUser)
        return ResponseEntity.ok(authUser)
    }

    @Operation(summary = "Deleting a user")
    @DeleteMapping("/delete")
    //@Transactional
    ResponseEntity<Map<String, String>> deleteUser(@AuthenticationPrincipal User user) {
        postRepository.deleteByUser(user)
        refreshTokenRepository.deleteByUser(user)
        userService.delete(user)
        return ResponseEntity.ok(Map.of("description", "User successfully deleted"))
    }
}
