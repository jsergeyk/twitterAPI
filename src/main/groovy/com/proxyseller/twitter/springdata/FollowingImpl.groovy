package com.proxyseller.twitter.springdata

import com.proxyseller.twitter.document.Following
import com.proxyseller.twitter.document.User
import com.proxyseller.twitter.repository.FollowingRepository
import org.springframework.stereotype.Repository

@Repository
class FollowingImpl implements IFollowing {

    private final FollowingRepository followingRepository

    FollowingImpl(FollowingRepository followingRepository) {
        this.followingRepository = followingRepository
    }

    @Override
    Following save(Following following) {
        return followingRepository.save(following)
    }

    @Override
    Optional<Following> findById(String id) {
        return followingRepository.findById(id);
    }

    @Override
    void delete(Following following) {
        followingRepository.delete(following)
    }

    @Override
    List<Following> findByUser(User user) {
        return followingRepository.findByUser(user)
    }
}