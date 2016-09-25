package org.evey.service;

import org.springframework.security.core.Authentication;

/**
 * Created by Laurie on 25 Sep 2016.
 */
public interface LoginService{
    public Authentication authenticate(Authentication authentication);

    public boolean supports(Class<?> authentication);
}
