package com.example.springjwt.jwt;

import com.example.springjwt.dto.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
   // post  http://localhost:8080?username=admin&password=1111
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        log.info("Attempting to authenticate username: " + username);
        log.info("Attempting to authenticate password: " + password);

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(username, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        Authentication authentication = authenticationManager.authenticate(authToken);

        log.info("authentication {} ", authentication);
        return authentication;
    }

//post  http://localhost:8080
    // body - json
//    {
//        "username" : "admin",
//        "password" : "1111"
//    }
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
//
//            String username = loginRequest.getUsername();
//            String password = loginRequest.getPassword();
//
//            log.info("LoginFilter username ===> {} ", username);
//
//            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
//
//            return authenticationManager.authenticate(authToken);
//        }catch (Exception e)
//        { throw new RuntimeException("fatal error");}
//    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        System.out.println("[ROLE]]"+role);
        String token = jwtUtil.createJwt(username, role, 60*60*1000L);

        response.addHeader("Authorization", "Bearer " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}
