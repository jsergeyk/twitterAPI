package com.proxyseller.twitter.security

import com.proxyseller.twitter.service.UserService
import org.apache.logging.log4j.LogManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AccessTokenFilter extends OncePerRequestFilter {
    private static final def log = LogManager.getLogger(AccessTokenFilter.class)
    private static final def AUTHORIZATION_HEADER = "Authorization"
    private JwtHelper jwtHelper
    private UserService userService

    AccessTokenFilter(JwtHelper jwtHelper, UserService userService) {
        this.jwtHelper = jwtHelper
        this.userService = userService
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> token = parseToken(request)
            if (token.isPresent() && jwtHelper.validateAccessToken(token.get())) {
                def userId = jwtHelper.getUserIdFromAccessToken(token.get())
                def user = userService.findById(userId)
                def userAuth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
                userAuth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request))
                SecurityContextHolder.getContext().setAuthentication(userAuth)
            }
        } catch (Exception e) {
            log.error("error authentication", e)
        }
        filterChain.doFilter(request, response)
    }

    private Optional<String> parseToken(HttpServletRequest request) {
        def authHeader = request.getHeader(AUTHORIZATION_HEADER)
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.replace("Bearer ", ""))
        }
        return Optional.empty()
    }
}
