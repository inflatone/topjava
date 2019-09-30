package ru.javawebinar.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasEmail;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.web.ExceptionInfoHandler;

@Component
public class UniqueMailValidator implements Validator {
    @Autowired
    private UserRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return HasEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasEmail item = (HasEmail) target;
        User storedItem = repository.getByEmail(item.getEmail().toLowerCase());
        if (storedItem != null && !storedItem.getId().equals(item.getId())) {
            errors.rejectValue("email", ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL);
        }
    }
}