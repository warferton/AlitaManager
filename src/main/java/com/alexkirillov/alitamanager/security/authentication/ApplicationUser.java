package com.alexkirillov.alitamanager.security.authentication;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Set;

public class ApplicationUser implements UserDetails {
    @Id
    String id;
    @NotBlank
    private final String username;
    @NotBlank
    private final String password;
    @NotEmpty
    private final Set<? extends GrantedAuthority> grantedAuthorities;
    @NotBlank
    private final boolean isAccountNonExpired;
    @NotBlank
    private final boolean isAccountNonLocked;
    @NotBlank
    private final boolean isCredentialsNonExpired;
    @NotBlank
    private final boolean isEnabled;

    public ApplicationUser(@NotBlank String username,
                           @NotBlank String password,
                           @NotEmpty Set<? extends GrantedAuthority> grantedAuthorities,
                           @NotBlank boolean isAccountNonExpired,
                           @NotBlank boolean isAccountNonLocked,
                           @NotBlank boolean isCredentialsNonExpired,
                           @NotBlank boolean isEnabled) {

        this.grantedAuthorities = grantedAuthorities;
        this.password = password;
        this.username = username;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public String getId() {
        return id;
    }


}
