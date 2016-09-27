package org.ohf.validator;

import org.evey.bean.User;
import org.evey.utility.SecurityUtil;
import org.evey.validator.BaseValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * Created by Laurie on 9/17/2016.
 */
@Component("userValidator")
public class UserValidator extends BaseValidator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        if(user.getUsername()==null ||
                user.getUsername().length()<=0){
            errors.rejectValue("username", "username", "Please provide a Username");
        }

        if(user.getFirstName()==null ||
                user.getFirstName().length()<=0){
            errors.rejectValue("firstName", "firstName", "Please provide a First name");
        }

        if(user.getLastName()==null ||
                user.getLastName().length()<=0){
            errors.rejectValue("lastName", "lastName", "Please provide a Last name");
        }

        if(user.isNew() &&
                (user.getNewPassword()==null ||
                user.getNewPassword().length()<=0)){
            errors.rejectValue("password", "password", "Please provide a Password");
        }
    }
}
