package org.example.springkart.project.security.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.springkart.project.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtils {

    private static final Logger  logger= LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtCookieName}")
    private String JwtCookie;


    //Get the Jwt token from Header
    //Generating Token from UserName
    //Getting Username from the JWT Token
    //Generate Signing Key
    //validate JWT Token


    //Getting the JWT Token from Header
    public String getJwtFromHeader(HttpServletRequest request)
    {
        String bearerToken = request.getHeader("Authorization");
        logger.info("Bearer Token: "+bearerToken);
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);//7 because Bearer and a space (Bearer ) total 7
        }
        return null;
    }

    public String getJwtFromCookies(HttpServletRequest request){

        Cookie cookie= WebUtils.getCookie(request,JwtCookie);
        if(cookie!=null)
        {
            return cookie.getValue();
        }else
        {
            return null;
        }

    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal)
    {
        String jwtToken= generateTokenFromUserName(userPrincipal.getUsername());
       ResponseCookie cookie = ResponseCookie.from(JwtCookie,jwtToken)
               .path("/api")
               .maxAge(24*60*60)
               .httpOnly(false)
               .secure(false)//have done only for local developement!!
               .build();
       return cookie;
    }


    //Generating Token from UserName
    public String generateTokenFromUserName(String username)
    {
       //String username = userDetails.getUsername();
       return Jwts.builder()
               .subject(username)
               .issuedAt(new Date())
               .expiration(new Date((new Date().getTime()+ jwtExpirationMs)))
               .signWith(key())
               .compact();

    }

    //Getting Username from the JWT Token
    public String getUserNameFromJwtToken(String token)
    {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

    //Generate Signing Key
    public Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    //validate JWT Token
    public boolean validateJwtToken(String authToken)
    {
        try
        {
            System.out.println("VALIDATING JWT TOKEN.....");
            Jwts.parser()
                    .verifyWith((SecretKey)key())
                    .build()
                    .parseSignedClaims(authToken);
            System.out.println("✅ JWT is valid");
            return true;
        }catch (MalformedJwtException e)
        {
            System.out.println("❌ Invalid JWT: " + e.getMessage());
            logger.error("invalid JWT Token");

        }catch (ExpiredJwtException e)
        {
            logger.error("expired JWT Token");
        }catch (UnsupportedJwtException e)
        {
            logger.error("unsupported JWT Token");

        }catch (IllegalArgumentException e)
        {
            logger.error("invalid and illegal JWT Token");
        }
        return false;
    }



    public ResponseCookie getCleanJwtCookie()
    {
        ResponseCookie cookie = ResponseCookie.from(JwtCookie,null)
                .path("/api")
                .build();
        return cookie;
    }

}
