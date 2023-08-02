package com.proxyseller.twitter.rest;

import com.proxyseller.twitter.document.RefreshToken;
import com.proxyseller.twitter.document.User;
import com.proxyseller.twitter.dto.LoginDTO;
import com.proxyseller.twitter.dto.SignupDTO;
import com.proxyseller.twitter.dto.TokenDTO;
import com.proxyseller.twitter.repository.RefreshTokenRepository;
import com.proxyseller.twitter.security.JwtHelper;
import com.proxyseller.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    //@Transactional
    ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        def auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        def user = (User)auth.getPrincipal();

        def refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);
        def accessToken = jwtHelper.createAccessToken(user);
        def refreshTokenStr = jwtHelper.createRefreshToken(user, refreshToken.getId());
        return ResponseEntity.ok(new TokenDTO(user.getId(), accessToken, refreshTokenStr));
    }

    @PostMapping("/signup")
    //@Transactional
    ResponseEntity<?> signUp(@Valid @RequestBody SignupDTO signupDTO) {
        def user = new User(signupDTO.username(), signupDTO.email(), passwordEncoder.encode(signupDTO.password()), new HashSet<>(), true);
        userService.save(user);
        def refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);

        def accessToken = jwtHelper.createAccessToken(user);
        def refreshTokenStr = jwtHelper.createRefreshToken(user, refreshToken.getId());
        return ResponseEntity.ok(new TokenDTO(user.getId(), accessToken, refreshTokenStr));
    }

    @PostMapping("/logout")
    ResponseEntity<?> logout(@RequestBody TokenDTO dto) {
        def refreshTokenStr = dto.refreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenStr)) {
            def tokenId = jwtHelper.getTokenIdFromRefreshToken(refreshTokenStr);
            if (refreshTokenRepository.existsById(tokenId)) {
                refreshTokenRepository.deleteById(tokenId);
                return ResponseEntity.ok().build();
            }
        }
        throw  new BadCredentialsException("invalid token");
    }

    @PostMapping("/access-token")
    ResponseEntity<?> accessToken(@RequestBody TokenDTO dto) {
        def refreshTokenStr = dto.refreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenStr)) {
            String tokenId = jwtHelper.getTokenIdFromRefreshToken(refreshTokenStr);
            if (refreshTokenRepository.existsById(tokenId)) {
                User user = userService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenStr));
                String accessToken = jwtHelper.createAccessToken(user);
                return ResponseEntity.ok(new TokenDTO(user.getId(), accessToken, refreshTokenStr));
            }
        }
        throw  new BadCredentialsException("invalid token");
    }
}