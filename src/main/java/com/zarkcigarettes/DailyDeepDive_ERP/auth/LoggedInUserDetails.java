package com.zarkcigarettes.DailyDeepDive_ERP.auth;

import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.UserRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.User;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor

public class LoggedInUserDetails {
    private final UserRepository userRepository;

     public Optional<User> getLoggedUser(Authentication authentication){
        String username=authentication.getName();
        return selectUserByMyUsername(username);
    }

     public Optional<User> selectUserByMyUsername(String username) {
        return getAppUsers().stream()
                .filter(applicationUser -> username.equals(applicationUser.getEmail()))
                .findFirst();
    }
     public List<User> getAppUsers() {
        List<User> applicationUsers = Lists.newArrayList();
        applicationUsers = userRepository.findAll();
        return applicationUsers;
    }
}
