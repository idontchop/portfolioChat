package com.idontchop.portfolioChat.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Order(1)
@Component
public class JwtTokenService {
	static final long EXPIRATIONTIME = 864000000;	// 1 day
	
	
	static String SIGNINGKEY;
	
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

	public  String getSIGNINGKEY() { 
		return SIGNINGKEY;
	}

	@Value ("${jwt.secret}")
	public void setSIGNINGKEY(String sIGNINGKEY) {
		SIGNINGKEY = sIGNINGKEY;
	}

	

}
