package com.musicmy.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.musicmy.helper.JWTHelper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter implements Filter {

    @Autowired
    private JWTHelper oJWTHelper;

    @Override
    public void doFilter(ServletRequest oServletRequest, ServletResponse oServletResponse, FilterChain oFilterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) oServletRequest;
        HttpServletResponse response = (HttpServletResponse) oServletResponse;

        if ("OPTIONS".equals((request).getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            oFilterChain.doFilter(oServletRequest, oServletResponse);
        }
        // Si no va poner else
        String sToken = request.getHeader("Authorization");
        if (sToken == null) {
            oFilterChain.doFilter(oServletRequest, oServletResponse);
        } else {
            if (!sToken.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token no válido");
                return;
            } else {
                String token = sToken.substring(7);
                // validar (pendiente)
                String email = oJWTHelper.validateToken(token);

                if (email == null) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token no valido");
                    return;
                } else {
                    request.setAttribute("email", email);
                    oFilterChain.doFilter(oServletRequest, oServletResponse);
                }
            }

        }
    }

}