package com.alexkirillov.alitamanager.security.authentication.dao;

import com.alexkirillov.alitamanager.security.authentication.ApplicationUser;

import java.util.Optional;


public interface ApplicationUserDao {

    Optional<ApplicationUser> selectUserByUserName(String username);
}
