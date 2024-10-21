package com.cl3.alumnosbarrera.security;

import com.cl3.alumnosbarrera.model.Auth;
import com.cl3.alumnosbarrera.serviceImplement.UserDetailImplement;
import com.cl3.alumnosbarrera.util.Token;
import com.cl3.alumnosbarrera.repository.UsuarioRepository;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        Auth authCredenciales = new Auth();

        try {
            authCredenciales = new ObjectMapper().readValue(request.getReader(), Auth.class);
        } catch (Exception e) {
        }

        UsernamePasswordAuthenticationToken userPAT = new UsernamePasswordAuthenticationToken(
                authCredenciales.getEmail(),
                authCredenciales.getPassword(),
                Collections.emptyList()
        );

        return getAuthenticationManager().authenticate(userPAT);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UserDetailImplement userDetails = (UserDetailImplement) authResult.getPrincipal();
        String token = Token.crearToken(userDetails.getNombre(), userDetails.getUsername());

        response.addHeader("Authorization", "Bearer "+ token);
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }

}
