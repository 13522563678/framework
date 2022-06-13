package com.kcwl.framework.rest.exception;

import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.api.ResponseMessage;
import com.kcwl.ddd.infrastructure.exception.BaseException;
import com.kcwl.ddd.infrastructure.exception.BizException;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.framework.rest.helper.ResponseDecorator;
import com.kcwl.framework.utils.ServiceHttpStatus;
import com.kcwl.framework.utils.StringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
@ControllerAdvice
public class GlobalExceptionHandler extends AbstractExceptionHandler {

    @Resource
    ResponseDecorator responseDecorator;

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
        return fail(e.getCode(), e.getMessage(), null);
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
        String errorMesssage = getValidationMessage(violations);
        return fail(CommonCode.FIELD_ERROR.getCode(), CommonCode.FIELD_ERROR.getDescription() + errorMesssage, null);
    }

    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity methodValidateExceptionHandler(HttpServletRequest request, Exception e) {
        printRequest(request, e);
        return fail(CommonCode.FIELD_ERROR.getCode(), CommonCode.FIELD_ERROR.getDescription(), e);
    }


    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity missingSParameterExceptionHandler(HttpServletRequest request, Exception e) {
        printRequest(request, e);
        MissingServletRequestParameterException exp = (MissingServletRequestParameterException) e;
        String errorMesssage = String.format("：%s不能为空！", exp.getParameterName());
        return fail(CommonCode.FIELD_NULL.getCode(), CommonCode.FIELD_NULL.getDescription(), e);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exceptionHandler(HttpServletRequest request, Exception e) {
        printRequest(request, e);
        return fail(CommonCode.FAIL.getCode(), CommonCode.FAIL.getDescription(), e);
    }

    /**
     * 根据指定错误码返回结果
     *
     * @param code
     * @param message
     * @return
     */
    public ResponseEntity fail(String code, String message, Exception exception) {
        ResponseMessage responseMessage = createResponseMessage(code, message);
        String clientType = SessionContext.getRequestClient();
        //RequestUserAgent requestUserAgent = SessionHelper.getRequestUserAgent();
        //如果是服务调用，为了方便能够在调用方捕获异常
        if ((clientType != null) && (UserAgent.AGENT_CLIENT_FEIGN.equals(clientType))) {
            if (exception instanceof MethodArgumentNotValidException) {
                responseMessage.setMessage(queryArgumentDetailMessage((MethodArgumentNotValidException) exception));
            } else if (exception != null) {
                responseMessage.setMessage(exception.getMessage());
            }
            return ResponseEntity.status(ServiceHttpStatus.SERVICE_EXCEPTION_STATUS).body(responseMessage);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(responseMessage);
    }

    private String queryArgumentDetailMessage(MethodArgumentNotValidException methodArgumentException) {
        if (null == methodArgumentException || null == methodArgumentException.getBindingResult()) {
            return methodArgumentException.getMessage();
        }
        String argumentNotValidMessage = methodArgumentException.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(";"));
        if (StringUtil.isEmpty(argumentNotValidMessage)) {
            return methodArgumentException.getMessage();
        }
        return argumentNotValidMessage;
    }

    private String getValidationMessage(BindingResult bindingResult) {
        String errorMesssage = "";
        for (FieldError fieldError : bindingResult.getFieldErrors()) {

            if (errorMesssage.length() == 0) {
                errorMesssage = ":";
            } else {
                errorMesssage += ",";
            }
            errorMesssage += fieldError.getDefaultMessage();
        }
        return errorMesssage;
    }

    private String getValidationMessage(Set<ConstraintViolation<?>> bindingResult) {
        String errorMesssage = "";
        for (ConstraintViolation<?> fieldError : bindingResult) {
            if (errorMesssage.length() == 0) {
                errorMesssage = ":";
            } else {
                errorMesssage += ",";
            }
            errorMesssage += fieldError.getMessage();
        }
        return errorMesssage;
    }

    private ResponseMessage createResponseMessage(String code, String message) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(responseDecorator.paddingResponseCode(code));
        responseMessage.setMessage(message);
        return responseMessage;
    }

}
