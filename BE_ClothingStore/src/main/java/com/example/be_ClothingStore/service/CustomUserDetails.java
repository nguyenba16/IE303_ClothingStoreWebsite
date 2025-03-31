package com.example.be_ClothingStore.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private String id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public String getId() { 
        return id; 
    }

    @Override
    public String getUsername() { 
        return email; 
    }

    @Override
    public String getPassword() { 
        return password; 
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { 
        return authorities; 
    }

    @Override
    public boolean isAccountNonExpired() { 
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() { 
        return true; 
    }

    @Override
    public boolean isCredentialsNonExpired() { 
        return true; 
    }

    @Override
    public boolean isEnabled() { 
        return true; 
    }
}
