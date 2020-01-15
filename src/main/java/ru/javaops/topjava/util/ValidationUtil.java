package ru.javaops.topjava.util;

import ru.javaops.topjava.HasId;
import ru.javaops.topjava.util.exeption.NotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidationUtil {
    private static final Validator validator;

    static {
        // From Javadoc: implementations are thread-safe and instances are typically cached and reused
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        // From Javadoc: implementations of this interface must be thread-safe
        validator = validatorFactory.getValidator();
    }

    public static <T> void validate(T bean) {
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String message) {
        checkNotFound(object != null, message);
        return object;
    }

    public static void checkNotFound(boolean found, String message) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + message);
        }
    }

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalArgumentException(bean + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
        // conservative when you reply, but accept liberally
        // http://stackoverflow.com/a/32728226/548473
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new IllegalArgumentException(bean + " must be with id=" + id);
        }
    }

    // http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;
        while (null != (cause = result.getCause()) && result != cause) {
            result = cause;
        }
        return result;
    }
}
