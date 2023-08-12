package com.proxyseller.twitter.springdata;

import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.repository.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserImpl implements IUser {

    private final UserRepository userRepository

    UserImpl(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    @Override
    Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
    }

    @Override
    User save(User user) {
        return userRepository.save(user)
    }

    @Override
    Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
    }

    @Override
    Optional<User> findById(String id) {
        return userRepository.findById(id)
    }

    @Override
    void deleteByUsername(String username) {
        userRepository.deleteByUsername(username)
    }

    @Override
    void delete(User user) {
        userRepository.delete(user)
    }

    @Override
    boolean existsById(String id) {
        userRepository.existsById(id)
    }
}