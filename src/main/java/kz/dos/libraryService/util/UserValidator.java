package kz.dos.libraryService.util;

import kz.dos.libraryService.models.User;
import kz.dos.libraryService.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User)target;
        if(userService.getByFullName(user.getFullName()).isPresent()){
            errors.rejectValue("fullName", "", "User by this name already exists");
        }
    }

}
