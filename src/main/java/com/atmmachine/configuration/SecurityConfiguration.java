package com.atmmachine.configuration;

import com.atmmachine.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String[] AUTH_WHITELIST = {
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
            "/h2-console/**",
            "/swagger-ui.html",
    };

    @Autowired
    private SecurityService securityService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .defaultSuccessUrl("/detailPage.html", true)
                .and().rememberMe()
                .and().logout().logoutUrl("/logout") .logoutSuccessUrl("/login") .deleteCookies("remember-me")
                .and().headers().frameOptions().sameOrigin();
    }
}
