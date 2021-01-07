package com.alexkirillov.alitamanager.security.authentication.dao;

import com.alexkirillov.alitamanager.security.authentication.ApplicationUser;
import com.google.common.collect.Lists;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.alexkirillov.alitamanager.security.ApplicationUserRole.*;

@Repository("fake")
public class FakeApplicationUserService implements ApplicationUserDao {

    private PasswordEncoder passwordEncoder;

    public FakeApplicationUserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectUserByUserName(String username) {
        return getApplicationUsers().stream().filter(
                applicationUser -> username.equals(applicationUser.getUsername())
        ).findFirst();
    }

    public List<ApplicationUser> getApplicationUsers(){
        List<ApplicationUser> applicationUsers = Lists.newArrayList(
            new ApplicationUser(
                    "annasmith",
                    passwordEncoder.encode("password"),
                    ADMIN.getGrantedAuthorities(),
                    true,
                    true,
                    true,
                    true
            ),
                new ApplicationUser(
                        "anita",
                        passwordEncoder.encode("password1"),
                        EMPLOYEE.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        "tom",
                        passwordEncoder.encode("password2"),
                        CLIENT.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                )
        );

        return applicationUsers;
    }

}
