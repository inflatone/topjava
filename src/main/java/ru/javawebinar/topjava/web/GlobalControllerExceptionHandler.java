package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ApplicationException;
import ru.javawebinar.topjava.util.exception.ErrorType;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @Autowired
    private MessageUtil messageUtil;

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView wrongRequest(HttpServletRequest request, NoHandlerFoundException e) throws Exception {
        return logAndGetExceptionView(request, e, false, ErrorType.WRONG_REQUEST, null);
    }

    @ExceptionHandler(ApplicationException.class)
    public ModelAndView applicationErrorHandler(HttpServletRequest request, ApplicationException e) throws Exception {
        return logAndGetExceptionView(request, e, true, e.getType(), messageUtil.getMessage(e));
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        return logAndGetExceptionView(request, e, true, ErrorType.APP_ERROR, null);
    }

    private ModelAndView logAndGetExceptionView(HttpServletRequest request, Exception e, boolean logException, ErrorType errorType, String message) {
        Throwable rootCause = ValidationUtil.logAndGetRootCause(log, request, e, logException, errorType);
        ModelAndView mav = new ModelAndView("exception/exception");
        mav.addObject("typeMessage", messageUtil.getMessage(errorType.getErrorCode()));
        mav.addObject("exception", rootCause);
        mav.addObject("message", message != null ? message : ValidationUtil.getMessage(rootCause));
        return mav;
    }
}
