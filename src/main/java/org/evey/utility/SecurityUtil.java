package org.evey.utility;

import org.apache.log4j.Logger;
import org.evey.security.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Laurie on 1/19/2016.
 */
public class SecurityUtil {

    private static Logger _log = Logger.getLogger(SecurityUtil.class);

    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static SessionUser getSessionUser(){
        try {
            Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(object instanceof SessionUser){
                return ((SessionUser) object);
            }
        } catch (NullPointerException npe){
            _log.warn("No Security Context Found!");
        } catch (Exception e){
            _log.error(e.getMessage());
        }

        return null;
    }

    public static String encryptPassword(String cleartext) {
        if (passwordEncoder != null) {
            return passwordEncoder.encode(cleartext);
        }
        return cleartext;
    }

    public static Boolean isMatchPassword(String password, String testPassword){
        if (passwordEncoder != null){
            if (passwordEncoder.matches(testPassword,password)){
                return true;
            }
        }
        return false;
    }
}
