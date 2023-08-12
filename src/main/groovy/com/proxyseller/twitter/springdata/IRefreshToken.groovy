package com.proxyseller.twitter.springdata

import com.proxyseller.twitter.document.RefreshToken
import com.proxyseller.twitter.document.User

interface IRefreshToken {

        RefreshToken save(RefreshToken token)

        boolean existsById(String id)

        void delete(RefreshToken token)

        void deleteById(String id)

        void deleteByUser(User user)
}