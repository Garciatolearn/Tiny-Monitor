package org.garcia.monitor.server.config;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.garcia.monitor.server.dao.UserDetailMapper;
import org.garcia.monitor.server.entity.po.UserDetailPO;
import org.garcia.monitor.server.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.management.Query;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder bcryptPasswordEncoder(){
        return new BCryptPasswordEncoder(16);
    }

    @Bean
    @DependsOn("bcryptPasswordEncoder")
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
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests
                (conf -> conf.anyRequest().hasAnyRole(UserRole.ADMIN,UserRole.USER))
                .formLogin(login -> login.loginProcessingUrl("/api/auth/login").notifyAll())
                .csrf(AbstractHttpConfigurer::disable)
                ;
        return httpSecurity.build();
    }
}
