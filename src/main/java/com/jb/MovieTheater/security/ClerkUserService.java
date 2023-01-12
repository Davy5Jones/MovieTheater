package com.jb.MovieTheater.security;

import com.jb.MovieTheater.beans.MyUserDetails;
import com.jb.MovieTheater.repos.ClerkRepository;
import com.jb.MovieTheater.repos.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClerkUserService  implements UserDetailsService {
    private final ClerkRepository clerkRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return clerkRepository.findByEmail(username)
                .map(MyUserDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found: " + username));
    }
}
