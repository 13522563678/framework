package com.kcwl.framework.rest.web.filter;

import com.kcwl.framework.rest.web.filter.reqeust.ContentCachingRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 姚华成
 * @date 2018-01-30
 */
public class ContentCacheFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 解决出现异常时无法打印出Body的问题
        HttpServletRequest requestToUse = request;
        if (!(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request);
        }
        filterChain.doFilter(requestToUse, response);
    }
}
