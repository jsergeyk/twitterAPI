package com.proxyseller.twitter.service

import com.proxyseller.twitter.document.Following
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.dto.FollowingDTO
import com.proxyseller.twitter.exception.PropertyNotFoundException
import com.proxyseller.twitter.repository.FollowingRepository
import com.proxyseller.twitter.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FollowingService {

    @Autowired
    private FollowingRepository followingRepository
    @Autowired
    private UserRepository userRepository

    List<Following> findByUser(User user) {
        return followingRepository.findByUser(user)
    }

    Following save(Following following) {
        return followingRepository.save(following)
    }

    void delete(Following following) {
        followingRepository.delete(following)
    }

    Optional<User> findFollowingUser(FollowingDTO dto) {
        if (!dto.followingUserId()) {
            throw new PropertyNotFoundException(dto.followingUserId())
        }
        def followingUser = userRepository.findById(dto.followingUserId())
        if (!followingUser.isPresent()) {
            throw new PropertyNotFoundException(dto.followingUserId())
        }
        return followingUser
    }
}
