package com.proxyseller.twitter.springdata

import com.proxyseller.twitter.document.User

interface IUser {

        Optional<User> findByUsername(String username)

        Optional<User> findByEmail(String email)

        Optional<User> findById(String id)

        User save(User user)

        void deleteByUsername(String username)

        void delete(User user)

        boolean existsById(String id)
}