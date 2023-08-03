package com.proxyseller.twitter.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.proxyseller.twitter.document.User
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtHelper {
    private static final def ISSUER = "TwitterApp"
    private static final def TOKEN_ID = "tokenId"
    private static final def log = LogManager.getLogger(JwtHelper)

    private final int accessTokenExpirationMs
    private final long refreshTokenExpirationMs
    private final Algorithm accessTokenAlgorithm
    private final Algorithm refreshTokenAlgorithm
    private final JWTVerifier accessTokenVerifier
    private final JWTVerifier refreshTokenVerifier

    JwtHelper(@Value('${accessTokenSecret}') String accessTokenSecret,
                     @Value('${refreshTokenSecret}') String refreshTokenSecret,
                     @Value('${accessTokenExpirationMinutes}') int accessTokenExpirationMinutes,
                     @Value('${refreshTokenExpirationDays}') int refreshTokenExpirationDays) {
        accessTokenExpirationMs = accessTokenExpirationMinutes * 60 * 1000
        refreshTokenExpirationMs = ((long) refreshTokenExpirationDays * 24  * 60 * 60 * 1000)
        accessTokenAlgorithm = Algorithm.HMAC512(accessTokenSecret)
        refreshTokenAlgorithm = Algorithm.HMAC512(refreshTokenSecret)
        accessTokenVerifier = JWT.require(accessTokenAlgorithm).withIssuer(ISSUER).build()
        refreshTokenVerifier = JWT.require(refreshTokenAlgorithm).withIssuer(ISSUER).build()
    }

    String createAccessToken(User user) {
        Date now = new Date()
        return JWT.create().withIssuer(ISSUER)
                .withSubject(user.getId())
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationMs))
                .sign(accessTokenAlgorithm)
    }

    String createRefreshToken(User user, String tokenId) {
        def now = new Date()
        return JWT.create().withIssuer(ISSUER)
                .withSubject(user.getId())
                .withClaim(TOKEN_ID, tokenId)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationMs))
                .sign(refreshTokenAlgorithm)
    }

    private Optional<DecodedJWT> decodeAccessToken(String token) {
        try {
            return Optional.of(accessTokenVerifier.verify(token))
        } catch (JWTVerificationException e) {
            log.error("invalid access token " + token)
        }
        return Optional.empty()
    }

    private Optional<DecodedJWT> decodeRefreshToken(String token) {
        try {
            return Optional.of(refreshTokenVerifier.verify(token))
        } catch (JWTVerificationException e) {
            log.error("invalid refresh token " + token)
        }
        return Optional.empty()
    }

    Boolean validateAccessToken(String token) {
        return decodeAccessToken(token).isPresent()
    }

    Boolean validateRefreshToken(String token) {
        return decodeRefreshToken(token).isPresent()
    }

    String getUserIdFromAccessToken(String token) {
        return decodeAccessToken(token).get().getSubject()
    }

    String getUserIdFromRefreshToken(String token) {
        return decodeRefreshToken(token).get().getSubject()
    }

    String getTokenIdFromRefreshToken(String token) {
        return decodeRefreshToken(token).get().getClaim(TOKEN_ID).asString()
    }
}