package com.example.ativoeoperante.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class AccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token= ((HttpServletRequest)servletRequest).getHeader("Authorization");
        if(token != null && JWTTokenProvider.verifyToken(token)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).setStatus(401); // 401 é o código padrão para Unauthorized
            servletResponse.getOutputStream().write("Não autorizado".getBytes());
        }
    }
}
