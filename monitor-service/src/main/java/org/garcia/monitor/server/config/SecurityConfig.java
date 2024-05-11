package org.garcia.monitor.server.config;


import common.core.json.JsonUtils;
import common.core.jwt.JWTutils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.garcia.monitor.server.entity.Results;
import org.garcia.monitor.server.entity.po.UserPO;
import org.garcia.monitor.server.entity.resp.AccountVO;
import org.garcia.monitor.server.enums.UserRole;
import org.garcia.monitor.server.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig {


    @Resource
    JWTutils jwTutils;

    @Resource
    JsonUtils jsonUtils;

    @Resource
    UserService userService;

    @Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder(16);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return (username) -> {
            UserPO userFromDataBase = userService.findUserByUsernameOrEmail(username);
            return User.builder().username(username)
                    .password(userFromDataBase.getPassword())
                    .authorities(userFromDataBase.getUserRole())
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests
                        (conf -> conf.requestMatchers("/monitor-client/register")
                                .hasAnyRole(UserRole.USER)
                                .anyRequest().hasAnyRole(UserRole.ADMIN, UserRole.USER)
                        )
                .formLogin(login -> login.loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                        .failureHandler(this::onAuthenticationFailure)
                        .permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exc -> exc.accessDeniedHandler(
                        (request, response, accessDeniedException) -> {
                            response.setContentType("application/json;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(jsonUtils.toJson(Results
                                    .failure(accessDeniedException.getMessage()
                                            , "权限不足,无法访问")));
                        }
                ).authenticationEntryPoint(((request, response, authException) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(jsonUtils.toJson(Results.failure(authException.getMessage()
                            , "未登录,无法访问")));
                })))

                .build();
    }

    private void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        User details = (User) authentication.getPrincipal();

        UserPO userByUsernameOrEmail = userService
                .findUserByUsernameOrEmail(details.getUsername());

        AccountVO accountVO = new AccountVO(userByUsernameOrEmail.getUserName(),
                userByUsernameOrEmail.getId());
        writer.write(jsonUtils.toJson(Results.success(jwTutils.createToken(accountVO), "登录成功")));
    }

    private void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException{
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(jsonUtils.toJson(Results.failure(exception.getMessage(), "登录失败!")));
    }
}
