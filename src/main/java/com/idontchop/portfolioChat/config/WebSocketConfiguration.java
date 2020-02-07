package com.idontchop.portfolioChat.config;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;

import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements
	WebSocketMessageBrokerConfigurer {
	Logger logger = LoggerFactory.getLogger(WebSocketConfiguration.class);	

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/socket").setAllowedOrigins("*").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		logger.info("MessageBroker init...");
		registry.enableSimpleBroker("/secured/user/queue/specific-user");
		registry.setApplicationDestinationPrefixes("/react-chat");
		registry.setUserDestinationPrefix("/secured/user");
	}
	
}
