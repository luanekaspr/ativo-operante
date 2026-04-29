package com.example.ativoeoperante.security;
import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        if(token!=null && JWTTokenProvider.verifyToken(token)) {
            String nivel= JWTTokenProvider.getAllClaimsFromToken(token).get("nivel").toString();
            if(nivel.equals("CIDADAO")){
                if(req.getRequestURI().endsWith("denuncias/all")){
                    chain.doFilter(request, response);
                }
            }
            else
                chain.doFilter(request, response);
        }
        //else{
        ((HttpServletResponse)response).setStatus(500);
        response.getOutputStream().write("Não autorizado ".getBytes());
        // }
    }
}
