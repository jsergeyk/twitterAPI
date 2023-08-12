package com.proxyseller.twitter.springdata

import com.proxyseller.twitter.document.Following
import com.proxyseller.twitter.document.User

interface IFollowing {

        Optional<Following> findById(String id)

        Following save(Following user)

        void delete(Following like)

        List<Following> findByUser(User user)
}