package com.proxyseller.twitter.rest

import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.UserDTO
import com.proxyseller.twitter.service.PostService
import com.proxyseller.twitter.service.UserService
import com.proxyseller.twitter.springdata.IRefreshToken
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
    private PostService postService
    @Autowired
    private IRefreshToken refreshTokenRepository
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Editing a current user")
    @PutMapping(value = "/{id}")
    @PreAuthorize("#authUser.id == #id")
    ResponseEntity<?> editUserPut(@AuthenticationPrincipal User authUser, @PathVariable String id, @RequestBody UserDTO userDto) {
        authUser.email = userDto.email()
        authUser.username = userDto.username()
        authUser.isActive = userDto.isActive()
        userService.save(authUser)
        return ResponseEntity.ok(new UserDTO(authUser.username, authUser.email, null, authUser.isActive))
    }

    @Operation(summary = "Editing a user")
    @PatchMapping(value = "/{id}")
    @PreAuthorize("#authUser.id == #id")
    ResponseEntity<?> editUser(@AuthenticationPrincipal User authUser, @PathVariable String id, @RequestBody UserDTO userDto) {
        if (userDto.email() != null) {
            authUser.email = userDto.email()
        }
        if (userDto.username() != null) {
            authUser.username = userDto.username()
        }
        if (userDto.isActive() != null) {
            authUser.isActive = userDto.isActive()
        }
        if (userDto.password() != null) {
            authUser.setPassword(passwordEncoder.encode(userDto.password()))
        }
        userService.save(authUser)
        return ResponseEntity.ok(new UserDTO(authUser.username, authUser.email, null, authUser.isActive))
    }

    @Operation(summary = "Deleting a current user")
    @DeleteMapping("/{id}")
    @PreAuthorize("#user.id == #id")
    //@Transactional
    ResponseEntity<Map<String, String>> deleteUser(@AuthenticationPrincipal User user,  @PathVariable String id) {
        postService.deleteByUser(user)
        refreshTokenRepository.deleteByUser(user)
        userService.delete(user)
        return ResponseEntity.ok(Map.of("description", "User successfully deleted"))
    }
}
