package org.garcia.monitor.server.filter;

import cn.hutool.http.server.HttpServerRequest;
import common.config.JWTConfig;
import common.core.jwt.JWTutils;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.garcia.monitor.server.entity.resp.AccountVO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Resource
    JWTutils jwTutils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authentication = request.getHeader("Authentication");
        if( authentication == null ){
            log.info("未登录,无法进行该操作");
            filterChain.doFilter(request,response);
            return;
        }
        AccountVO accountVO = jwTutils.toObject(authentication, new AccountVO());
        //todo extract to be utils...singletonRole to be simple one role >> RBAC0
        UserDetails user = User.builder().
                username(accountVO.getUsername())
                .password("it doesn't make sense...")
                .authorities(accountVO.getUserRole()).build();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken
                (user,null,user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request,response);
    }
}
