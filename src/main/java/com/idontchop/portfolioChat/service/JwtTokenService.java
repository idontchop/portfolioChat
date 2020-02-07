package com.idontchop.portfolioChat.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Order(1)
public class JwtTokenService {
	static final long EXPIRATIONTIME = 864000000;	// 1 day
	static final String SIGNINGKEY = "SecretKey1";
	static final String PREFIX = "Bearer";
	
	static Logger logger = LoggerFactory.getLogger(JwtTokenService.class);
	

	// Get username from request
	static public String getAuthentication( HttpServletRequest req ) {
		
		String token = req.getHeader("Authorization");
		return getAuthenticationFromString(token);

	}
	
	static public String getAuthenticationFromString (String token) {
		if ( token != null && StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			
			logger.info("Found Auth Token");
			String user = Jwts.parser()
					.setSigningKey(SIGNINGKEY)
					.parseClaimsJws(token.replace(PREFIX, ""))
					.getBody()
					.getSubject();
			
			return user;
		}
		
		logger.info("Unauthenticated Request");
		return null;
	}


}
