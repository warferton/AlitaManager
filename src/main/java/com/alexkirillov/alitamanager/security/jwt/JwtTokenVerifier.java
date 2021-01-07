package com.alexkirillov.alitamanager.security.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    @Autowired
    public JwtTokenVerifier( JwtConfig jwtConfig, SecretKey secretKey) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

       String authorisationHeader = httpServletRequest.getHeader(jwtConfig.getAuthorisationHeader());

       if(Strings.isNullOrEmpty(authorisationHeader) || !authorisationHeader.startsWith(jwtConfig.getTokenPrefix())){
           filterChain.doFilter(httpServletRequest, httpServletResponse);
           return;
       }

           String token = authorisationHeader.replace(jwtConfig.getTokenPrefix(), "");
       try{

           Jws<Claims> claimsJws = Jwts.parserBuilder()
                   .setSigningKey(secretKey)
                   .build()
                   .parseClaimsJws(token);

           Claims body = claimsJws.getBody();

           String username = body.getSubject();

           List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

          Set<SimpleGrantedAuthority> simpleGrantedAuthorities =  authorities.stream()
                   .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                   .collect(Collectors.toSet());

           Authentication authentication = new UsernamePasswordAuthenticationToken(
                   username, null, simpleGrantedAuthorities
           );

           SecurityContextHolder.getContext().setAuthentication(authentication);
       }
       catch (JwtException e){
           System.err.println(e);
           throw new IllegalStateException(
                   String.format("Token Cannot Be Trusted. Token: %s", token));
       }


        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
