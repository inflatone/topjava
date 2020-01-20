package ru.javaops.topjava.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.javaops.topjava.util.ValidationUtil;
import ru.javaops.topjava.util.exeption.ErrorType;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javaops.topjava.util.ValidationUtil.getRootCause;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = getLogger(GlobalExceptionHandler.class);

    private final MessageUtil messageUtil;

    @Autowired
    public GlobalExceptionHandler(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        log.error("Exception at request " + request.getRequestURL(), e);
        Throwable rootCause = getRootCause(e);

        var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        var modelAndView = new ModelAndView("exception",
                Map.of("exception", rootCause,
                        "message", ValidationUtil.getMessage(rootCause),
                        "typeMessage", messageUtil.getMessage(ErrorType.APP_ERROR.getErrorCode()),
                        "status", httpStatus)
        );
        modelAndView.setStatus(httpStatus);

        // Interceptor is not invoked, put userTo
        var authorizedUser = SecurityUtil.safeGet();
        if (authorizedUser != null) {
            modelAndView.addObject("userTo", authorizedUser.getUserTo());
        }
        return modelAndView;
    }

}
