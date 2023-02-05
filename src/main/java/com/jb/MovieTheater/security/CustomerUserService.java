package com.jb.MovieTheater.security;

import com.jb.MovieTheater.beans.MyUserDetails;
import com.jb.MovieTheater.repos.CustomerRepository;
import com.jb.MovieTheater.repos.logs.CustomerLogsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerUserService implements UserDetailsService {
    private final CustomerLogsRepository customerLogsRepository;

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUserDetails myUserDetails = customerRepository.findByEmail(username)
                .map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
        customerLogsRepository.customerLogged(myUserDetails.getId());
        return myUserDetails;
    }

}
