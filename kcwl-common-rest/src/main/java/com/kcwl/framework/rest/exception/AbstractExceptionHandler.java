package com.kcwl.framework.rest.exception;

import com.google.gson.Gson;
import com.kcwl.framework.utils.RequestUtil;
import com.kcwl.framework.utils.StreamUtil;
import com.kcwl.framework.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author 姚华成
 */
@Slf4j
public class AbstractExceptionHandler {
    private static Gson gson = new Gson();

    protected void printRequest(HttpServletRequest request, Exception e) {
        String requestBody;
        try {
            String charsetName;
            requestBody = StreamUtil.copyToString(request.getInputStream(),
                    !StringUtil.isEmpty(charsetName = request.getCharacterEncoding()) ?
                            Charset.forName(charsetName) :
                            StandardCharsets.UTF_8);
        } catch (IOException ex) {
            requestBody = "";
        }
        log.warn("请求出错，错误信息为：{}\n" +
                        "请求客户端IP：{}\n请求地址：{}\n请求方法：{}\n请求头：{}\n请求参数：{}\n请求体：{}",
                e.getMessage(),
                RequestUtil.getClientIpAddr(request),
                request.getRequestURI(),
                request.getMethod(),
                gson.toJson(RequestUtil.getHeaderMap(request)),
                gson.toJson(request.getParameterMap()),
                requestBody,
                e
        );
    }
}
