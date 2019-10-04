package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorType;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @Autowired
    private MessageUtil messageUtil;

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView wrongRequest(HttpServletRequest request, NoHandlerFoundException e) throws Exception {
        return logAndGetExceptionView(request, e, false, ErrorType.WRONG_REQUEST);

    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {
        return logAndGetExceptionView(request, e, true, ErrorType.APP_ERROR);
    }

    private ModelAndView logAndGetExceptionView(HttpServletRequest request, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.logAndGetRootCause(log, request, e, logException, errorType);

        ModelAndView modelAndView = new ModelAndView("exception/exception");
        modelAndView.addObject("typeMessage", messageUtil.getMessage(errorType.getErrorCode()));
        modelAndView.addObject("exception", rootCause);
        modelAndView.addObject("message", ValidationUtil.getMessage(rootCause));
        return modelAndView;
    }
}