package com.proxyseller.twitter.springdata

import com.proxyseller.twitter.document.RefreshToken
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.repository.RefreshTokenRepository
import org.springframework.stereotype.Repository

@Repository
class RefreshTokenImpl implements IRefreshToken {

    private final RefreshTokenRepository refreshTokenRepository

    RefreshTokenImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository
    }

    @Override
    RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken)
    }

    @Override
    boolean existsById(String id) {
        return refreshTokenRepository.existsById(id)
    }

    @Override
    void delete(RefreshToken like) {
        refreshTokenRepository.delete(like)
    }

    @Override
    void deleteById(String id) {
        refreshTokenRepository.deleteById(id)
    }

    @Override
    void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user)
    }
}