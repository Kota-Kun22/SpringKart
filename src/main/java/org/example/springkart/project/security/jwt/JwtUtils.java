package org.example.springkart.project.security.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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


    //Generating Token from UserName
    public String generateTokenFromUserName(UserDetails userDetails)
    {
       String username = userDetails.getUsername();
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
            return true;
        }catch (MalformedJwtException e)
        {
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


}
