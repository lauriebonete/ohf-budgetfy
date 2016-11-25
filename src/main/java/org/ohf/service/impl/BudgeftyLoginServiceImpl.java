package org.ohf.service.impl;

import org.evey.bean.User;
import org.evey.security.SessionUser;
import org.evey.service.LoginService;
import org.evey.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laurie on 25 Sep 2016.
 */
@Service("loginService")
public class BudgeftyLoginServiceImpl implements LoginService {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();


        User user = userService.getUserByUsername(userName);

        if (user != null) {

            if(user.isMatchPassword(password)){
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user));
                SessionUser sessionUser = new SessionUser(userDetails);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(sessionUser, null, getGrantedAuthorities(user));
                SecurityContextHolder.getContext().setAuthentication(auth);
                return auth;
            }
        }

        return null;
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

       /* for(UserRole userRole : user.getUserRole()){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+userRole.getRole()));
        }*/
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
