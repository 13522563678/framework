package com.kcwl.framework.actuator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * actuator 安全性配置
 */
@Configuration
public class ActuatorSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ACTUATOR_PATH = "/actuator/**";
    private static final String ACTUATOR_ROLE = "ACTUATOR";

    @Value("${kcwl.actuator.security.username:actuator}")
    private String username;

    @Value("${kcwl.actuator.security.password:123123123}")
    private String password;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser(username)
                .password(passwordEncoder.encode(password))
                .roles(ACTUATOR_ROLE);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(ACTUATOR_PATH).hasRole(ACTUATOR_ROLE)
                .anyRequest().permitAll()
                .and().httpBasic()
                .and().formLogin()
                .and().csrf().disable();
    }

}
