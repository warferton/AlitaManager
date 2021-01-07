package com.alexkirillov.alitamanager.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.alexkirillov.alitamanager.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    CLIENT(Sets.newHashSet(
            SERVICES_READ, APPOINTMENTS_WRITE
    )),
    ADMIN(Sets.newHashSet(
            CLIENTS_READ, CLIENTS_WRITE, EMPLOYEES_READ, EMPLOYEES_WRITE, SERVICES_READ, SERVICES_WRITE,
            APPOINTMENTS_READ, APPOINTMENTS_WRITE, WORKDAYS_READ, WORKDAYS_WRITE
    )),
    EMPLOYEE(Sets.newHashSet(
            APPOINTMENTS_WRITE, APPOINTMENTS_READ, SERVICES_READ, WORKDAYS_READ
    ));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions(){
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}
