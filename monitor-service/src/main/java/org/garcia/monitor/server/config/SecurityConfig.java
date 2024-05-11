package org.garcia.monitor.server.config;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.garcia.monitor.server.dao.UserDetailMapper;
import org.garcia.monitor.server.entity.po.UserDetailPO;
import org.garcia.monitor.server.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.management.Query;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder bcryptPasswordEncoder(){
        return new BCryptPasswordEncoder(16);
    }

    @Bean
    public UserDetailsService userDetailsService(UserDetailMapper mapper){
        return(username) -> {
            UserDetailPO userFromDataBase = mapper.
                    selectOne(new QueryWrapper<UserDetailPO>()
                            .eq("user_name", username));
            return User.builder().username(username)
                    .password(userFromDataBase.getPassword())
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests
                (conf -> conf.anyRequest().hasAnyRole(UserRole.ADMIN,UserRole.USER))
                .formLogin(login -> login.loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                        .permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    private void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        writer.write(request.getParameter("username") + "登录成功");
    }
}
