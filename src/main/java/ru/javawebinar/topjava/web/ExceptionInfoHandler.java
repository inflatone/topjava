package ru.javawebinar.topjava.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static ru.javawebinar.topjava.util.exception.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    public static final String EXCEPTION_DUPLICATE_EMAIL = "exception.user.duplicateEmail";
    public static final String EXCEPTION_DUPLICATE_DATETIME = "exception.meal.duplicateDateTime";

    private static final Map<String, String> CONSTRAINS_I18N_MAP = Map.of(
            "users_unique_email_idx", EXCEPTION_DUPLICATE_EMAIL,
            "meals_unique_user_datetime_idx", EXCEPTION_DUPLICATE_DATETIME
    );

    @Autowired
    private MessageUtil messageUtil;

    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)    // 422
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest request, NotFoundException e) {
        return logAndGetErrorInfo(request, e, false, DATA_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)    // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo handleConflict(HttpServletRequest request, DataIntegrityViolationException e) {
        String rootMessage = ValidationUtil.getRootCause(e).getMessage();
        if (rootMessage != null) {
            String lowerCaseMessage = rootMessage.toLowerCase();
            Optional<Map.Entry<String, String>> entry = CONSTRAINS_I18N_MAP.entrySet().stream()
                    .filter(it -> lowerCaseMessage.contains(it.getKey()))
                    .findAny();
            if (entry.isPresent()) {
                return logAndGetErrorInfo(request, e, false, VALIDATION_ERROR, messageUtil.getMessage(entry.get().getValue()));
            }
        }
        return logAndGetErrorInfo(request, e, true, DATA_ERROR);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)    // 422
    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ErrorInfo handleIllegalRequestDataError(HttpServletRequest request, Exception e) {
        return logAndGetErrorInfo(request, e, false, VALIDATION_ERROR);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)    // 422
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ErrorInfo bindValidationError(HttpServletRequest request, Exception e) {
        BindingResult result = e instanceof BindException ?
                ((BindException) e).getBindingResult() : ((MethodArgumentNotValidException) e).getBindingResult();
        String[] details = result.getFieldErrors().stream().map(error -> {
            String message = error.getDefaultMessage();
            return message == null ? null
                    : message.startsWith(error.getField()) ? message : error.getField() + ' ' + message;
        }).filter(Objects::nonNull)
                .toArray(String[]::new);
        return logAndGetErrorInfo(request, e, false, VALIDATION_ERROR, details);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)   // 500
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest request, Exception e) {
        return logAndGetErrorInfo(request, e, true, APP_ERROR);
    }


    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest request, Exception e, boolean logException, ErrorType errorType, String... details) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + request.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request {}: {}", errorType, request.getRequestURL(), rootCause);
        }
        return new ErrorInfo(request.getRequestURL(), errorType,
                details.length != 0 ? details : new String[]{ValidationUtil.getMessage(rootCause)});
    }
}
