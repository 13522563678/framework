package com.kcwl.framework.rest.exception;

import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.domain.event.ExceptionEvent;
import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.api.ResponseMessage;
import com.kcwl.ddd.infrastructure.exception.BaseException;
import com.kcwl.ddd.infrastructure.exception.BizException;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.framework.rest.helper.ResponseHelper;
import com.kcwl.framework.utils.ServiceHttpStatus;
import com.kcwl.framework.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.*;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 姚华成
 * @date 2017-11-14
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends AbstractExceptionHandler {

    @Resource
    ExceptionEventPublisher exceptionEventPublisher;

    @ResponseBody
    @ExceptionHandler(value = BizException.class)
    public ResponseEntity bizExceptionHandler(HttpServletRequest request, BizException e) {
        printRequest(request, e);
        return fail(e.getCode().toString(), e.getMessage(), null);
    }

    @ResponseBody
    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity baseExceptionHandler(HttpServletRequest request, BaseException e) {
        printRequest(request, e);
        return fail(e.getCode(), e.getMessage(), e.getResult(), null);
    }


    @ResponseBody
    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity validateExceptionHandler(HttpServletRequest request, ValidationException e) {
        printRequest(request, e);
        return fail(CommonCode.FIELD_ERROR.getCode(), CommonCode.FIELD_ERROR.getDescription(), null);
    }

    @ResponseBody
    @ExceptionHandler(value = {BindException.class})
    public ResponseEntity beanPropertyBindingResultHandler(HttpServletRequest request, BindException e) {
        printRequest(request, e);
        BeanPropertyBindingResult bindingResult = (BeanPropertyBindingResult) e.getBindingResult();
        String errorMesssage = getValidationMessage(bindingResult);
        return fail(CommonCode.FIELD_ERROR.getCode(), errorMesssage, null);
    }

    /**
     * 分组校验异常
     *
     * @param request
     * @param e
     * @param
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity constraintViolationExceptionHandler(HttpServletRequest request, Exception e) {
        printRequest(request, e);
        Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) e).getConstraintViolations();
        String errorMessage = getValidationMessage(violations);
        return fail(CommonCode.FIELD_ERROR.getCode(), CommonCode.FIELD_ERROR.getDescription() + errorMessage, null);
    }

    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity methodValidateExceptionHandler(HttpServletRequest request, Exception e) {
        printRequest(request, e);
        MethodArgumentNotValidException ex = (MethodArgumentNotValidException)e;
        String validateMsg = getMethodValidateMessage(ex.getBindingResult(), CommonCode.FIELD_ERROR.getDescription());
        return fail(CommonCode.FIELD_ERROR.getCode(), validateMsg, e);
    }


    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity missingSParameterExceptionHandler(HttpServletRequest request, Exception e) {
        printRequest(request, e);
        MissingServletRequestParameterException exp = (MissingServletRequestParameterException) e;
        return fail(CommonCode.FIELD_NULL.getCode(), CommonCode.FIELD_NULL.getDescription(), e);
    }

    @ResponseBody
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity bodyEmptyExceptionHandler(HttpServletRequest request, Exception e) {
        printRequest(request, e);
        return fail(CommonCode.BODY_EMPTY_FAIL.getCode(), CommonCode.BODY_EMPTY_FAIL.getDescription(), e);
    }

    @ResponseBody
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity requestNotFoundExceptionHandler(HttpServletRequest request, Exception e) {
        printRequest(request, e);
        log.error("请求[{}]不存在", request.getRequestURI());
        return fail(CommonCode.REQUEST_NOT_FOUND.getCode(), CommonCode.REQUEST_NOT_FOUND.getDescription(), e);
    }

    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity requestMethodNotSupportedExceptionHandler(HttpServletRequest request, Exception e) {
        printRequest(request, e);
        log.error("{}不支持{}请求类型", request.getRequestURI(),request.getMethod());
        return fail(CommonCode.REQUEST_METHOD_NOT_SUPPORT.getCode(), CommonCode.REQUEST_METHOD_NOT_SUPPORT.getDescription(), e);
    }

    @ResponseBody
    @ExceptionHandler(value = MyBatisSystemException.class)
    public ResponseEntity myBatisSystemExceptionHandler(HttpServletRequest request, Exception e) {
        printRequest(request, e);
        log.error("数据库异常：[{}]", request.getRequestURI());
        return fail(CommonCode.ACCESS_DB_FAIL.getCode(), CommonCode.ACCESS_DB_FAIL.getDescription(), e);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exceptionHandler(HttpServletRequest request, Exception e) {
        printRequest(request, e);
        return fail(CommonCode.SERVICE_UNAVAILABLE.getCode(), CommonCode.SERVICE_UNAVAILABLE.getDescription(), e);
    }

    public ResponseEntity fail(String code, String message, Exception exception) {
        return fail(code, message, null, exception);
    }

    /**
     * 根据指定错误码返回结果
     *
     * @param code
     * @param message
     * @return
     */
    public ResponseEntity fail(String code, String message, Object result, Exception exception) {
        ResponseMessage responseMessage = createResponseMessage(code, message);
        String clientType = SessionContext.getRequestClient();
        //RequestUserAgent requestUserAgent = SessionHelper.getRequestUserAgent();
        responseMessage.setResult(result);

        if ((clientType != null) && (UserAgent.AGENT_CLIENT_FEIGN.equals(clientType))) {
            if (exception instanceof MethodArgumentNotValidException) {
                responseMessage.setMessage(queryArgumentDetailMessage((MethodArgumentNotValidException) exception));
            }
            //不再向调用方返回异常信息；
            //else if (exception != null) {
            //    responseMessage.setMessage(exception.getMessage());
            //}
            log.error("http-status={}, code={}, message={}", ServiceHttpStatus.SERVICE_EXCEPTION_STATUS, responseMessage.getCode(), responseMessage.getMessage());
            exceptionEventPublisher.publish(new ExceptionEvent(code, message, null, exception));
            return ResponseEntity.status(ServiceHttpStatus.SERVICE_EXCEPTION_STATUS).body(responseMessage);
        }
        log.error("http-status={}, code={}, message={}", HttpStatus.OK.value(), responseMessage.getCode(), responseMessage.getMessage());
        exceptionEventPublisher.publish(new ExceptionEvent(responseMessage.getCode(), responseMessage.getMessage(), null, exception));
        return ResponseEntity.status(HttpStatus.OK.value()).body(responseMessage);
    }

    private String queryArgumentDetailMessage(MethodArgumentNotValidException methodArgumentException) {
        if ( null == methodArgumentException ) {
            return "MethodArgumentNotValidException NullPointerException";
        }
        String argumentNotValidMessage = methodArgumentException.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(";"));
        if (StringUtil.isEmpty(argumentNotValidMessage)) {
            return methodArgumentException.getMessage();
        }
        return argumentNotValidMessage;
    }

    private String getValidationMessage(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            if (errorMessage.length() > 0) {
                errorMessage.append(";");
            }
            errorMessage.append(fieldError.getDefaultMessage());
        }
        return errorMessage.toString();
    }

    private String getMethodValidateMessage(BindingResult bindingResult, String defaultMsg) {
        StringBuilder sb = new StringBuilder();
        bindingResult.getAllErrors().forEach((error) -> {
            if ( sb.length() > 0 ) {
                sb.append("；");
            }
            sb.append(error.getDefaultMessage());
        });
        return (sb.length() > 0) ? sb.toString() : defaultMsg;
    }

    private String getValidationMessage(Set<ConstraintViolation<?>> bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        for (ConstraintViolation<?> fieldError : bindingResult) {
            if (errorMessage.length() > 0) {
                errorMessage.append(";");
            }
            errorMessage.append(fieldError.getMessage());
        }
        return errorMessage.toString();
    }

    private ResponseMessage createResponseMessage(String code, String message) {
        return ResponseHelper.createFailMessage(code, message);
    }
}
