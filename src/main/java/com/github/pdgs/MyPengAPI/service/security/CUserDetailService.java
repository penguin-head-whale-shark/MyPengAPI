package com.github.pdgs.MyPengAPI.service.security;

import com.github.pdgs.MyPengAPI.advice.exception.CUserNotFoundException;
import com.github.pdgs.MyPengAPI.repository.UserJpaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CUserDetailService implements UserDetailsService {

    private final UserJpaRepo userJpaRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userJpaRepo.findById(Long.valueOf(username)).orElseThrow(CUserNotFoundException::new);
    }
}
