package com.idontchop.portfolioChat.config;

import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Necessary to expose the authentication principals to our data rest queries
 * (without using prefilter and postfilter which would add overhead)
 * 
 * @author nathan
 *
 */
/*
public class SecurityEvaluationContextExtension implements EvaluationContextExtension {

	@Override
	public String getExtensionId() {
		
		return "security";

	}
	
	@Override
	public SecurityExpressionRoot getRootObject() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    return new SecurityExpressionRoot(authentication) {};
	}
	


}*/
