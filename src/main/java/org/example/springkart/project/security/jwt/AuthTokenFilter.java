package org.example.springkart.project.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springkart.project.security.services.UserDetailsServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    private static final Logger logger=  LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        logger.debug("AuthTokenFilter call for URI:{}", request.getRequestURI());

        // ✅ CHANGE 1 — Skip JWT check if request is for /images/**
        if (request.getRequestURI().startsWith("/images/")) {
            filterChain.doFilter(request, response);
            return;
        }
        try{
            String jwt = parseJwt(request);
            if(jwt!= null && jwtUtils.validateJwtToken(jwt))
            {
                String UserName =   jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(UserName);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                logger.debug("Roles from JWT: {}", userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));//attaching the request object to the authentication object

                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Roles from JWT : {}",  authentication.getAuthorities());

            }
        }catch (Exception e)
        {
            logger.error("can not set user authentication :{}",e.getMessage());

        }
        filterChain.doFilter(request, response);

    }

//    private String parseJwt(HttpServletRequest request) {
//        String jwt= jwtUtils.getJwtFromCookies(request);
//        logger.info("jwt token is here :{}",jwt);
//        return jwt;
//    }

private String parseJwt(HttpServletRequest request) {
    String jwtFromCookies= jwtUtils.getJwtFromCookies(request);
    if(jwtFromCookies !=null){
        return jwtFromCookies;
    }
    String jwtFromHeader= jwtUtils.getJwtFromHeader(request);
    if(jwtFromHeader !=null){
        return jwtFromHeader;
    }
    return null;
    }
}
