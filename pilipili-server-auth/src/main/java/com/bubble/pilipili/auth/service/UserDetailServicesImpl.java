package com.bubble.pilipili.auth.service;

import com.bubble.pilipili.auth.dto.UserDTO;
import com.bubble.pilipili.auth.entity.UserAuth;
import com.bubble.pilipili.auth.mapper.UserAuthMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@Service
public class UserDetailServicesImpl implements UserDetailsService {

    @Resource
    private UserAuthMapper userAuthMapper;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserAuth userAuth = userAuthMapper.selectById(username);
        if (userAuth == null) {
            throw new UsernameNotFoundException(username);
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userAuth.getUsername().toString());

        // test
        userAuth.setPassword(bCryptPasswordEncoder.encode(userAuth.getPassword()));

        userDTO.setPassword(userAuth.getPassword());

        return userDTO;
    }
}
