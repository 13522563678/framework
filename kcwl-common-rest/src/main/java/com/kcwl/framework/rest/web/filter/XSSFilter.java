package com.kcwl.framework.rest.web.filter;

import com.kcwl.framework.rest.web.filter.reqeust.XSSEscapeRequestWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * XSS 过滤器
 */
public class XSSFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(new XSSEscapeRequestWrapper(request), response);
    }

}
