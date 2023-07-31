package com.projectx.jwt.common.service;

import com.projectx.jwt.common.entity.CustomUserDetails;
import com.projectx.jwt.mysqldb.entity.Users;
import com.projectx.jwt.mysqldb.repository.UsersRepository;
import com.projectx.jwt.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class CustomUserService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = null;
        if (Constants.isMobile(username)) {
            users = usersRepository.findUsersByMobile(Long.parseLong(username));
        } else {
            users = usersRepository.findUsersByEmail(username);
        }

        if(users==null)
            throw new UsernameNotFoundException("User with email: " +username +" not found !");
        else {
            return new CustomUserDetails(users);
        }
    }
}
