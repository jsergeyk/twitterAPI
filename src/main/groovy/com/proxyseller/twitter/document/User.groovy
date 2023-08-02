package com.proxyseller.twitter.document

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mongodb.lang.NonNull
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document
class User implements UserDetails {

    @Id
    String id
    @Indexed(unique = true)
    @NonNull
    String username
    @Indexed(unique = true)
    @NonNull
    String email
    @JsonIgnore
    @NonNull
    String password
    Set<SimpleGrantedAuthority> authorities
    Boolean isActive

    User() {
    }

    User(String username, String email, String password, Set<SimpleGrantedAuthority> authorities, Boolean isActive) {
        this.username = username
        this.email = email
        this.password = password
        this.authorities = authorities
        this.isActive = isActive
    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities
    }
    @Override
    String getPassword() {
        return password
    }
    @Override
    String getUsername() {
        return username
    }
    @Override
    boolean isAccountNonExpired() {
        return isActive
    }
    @Override
    boolean isAccountNonLocked() {
        return isActive
    }
    @Override
    boolean isCredentialsNonExpired() {
        return isActive
    }
    @Override
    boolean isEnabled() {
        return isActive
    }

    @Override
    boolean equals(o) {
        if (this.is(o)) return true
        if (o == null || getClass() != o.class) return false

        User user = (User) o

        if (email != user.email) return false
        if (id != user.id) return false
        if (username != user.username) return false

        return true
    }

    @Override
    int hashCode() {
        int result
        result = id.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + email.hashCode()
        return result
    }
}
