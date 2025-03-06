/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.auth.service;

import com.bubble.pilipili.auth.config.ResourceConfigProperties;
import com.bubble.pilipili.auth.dto.UserDTO;
import com.bubble.pilipili.auth.entity.RoleMap;
import com.bubble.pilipili.auth.entity.UserAuth;
import com.bubble.pilipili.auth.mapper.UserAuthMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@Slf4j
@Service
public class UserDetailServicesImpl implements UserDetailsService {

    @Autowired
    private UserAuthMapper userAuthMapper;

//    @Autowired
//    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ResourceConfigProperties resourceConfigProperties;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserAuth userAuth = userAuthMapper.selectById(username);
        if (userAuth == null) {
            throw new UsernameNotFoundException(username);
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userAuth.getUsername().toString());

        // test  目前数据库查出的密码字段是明文，需要手动加密一下  后续存入数据库的密码字段是密文，则不需要执行这一步
//        userAuth.setPassword(bCryptPasswordEncoder.encode(userAuth.getPassword()));

        userDTO.setPassword(userAuth.getPassword());

        String roleNum = userAuth.getRole();
        String roleName = "";
        List<RoleMap> roleMap = resourceConfigProperties.getRoleMap();
        List<RoleMap> result = roleMap.stream()
                .filter(role -> role.getId().equals(roleNum))
                .collect(Collectors.toList());
        if (!result.isEmpty()) {
            roleName = result.get(0).getName();
        }
        Set<SimpleGrantedAuthority> authorities = (Set<SimpleGrantedAuthority>) userDTO.getAuthorities();
        authorities.add(new SimpleGrantedAuthority(roleName));

        log.debug("userDTO: {}", userDTO);
        return userDTO;
    }
}
