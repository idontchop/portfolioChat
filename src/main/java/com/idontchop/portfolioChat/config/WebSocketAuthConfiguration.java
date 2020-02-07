package com.idontchop.portfolioChat.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.idontchop.portfolioChat.service.JwtTokenService;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketAuthConfiguration implements WebSocketMessageBrokerConfigurer {
	
	Logger logger = LoggerFactory.getLogger(WebSocketAuthConfiguration.class);
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		logger.debug("Configuring WebSocket Security...");
		
		registration.interceptors( new ChannelInterceptor () {
						
	        @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
	        	logger.debug("Caught preSend...");
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authorization = accessor.getNativeHeader("Authorization");
                    logger.debug("Authorization: {}", authorization);                    
                    String user = JwtTokenService.getAuthenticationFromString(authorization.get(0));
                    logger.info(user);
                  
                    accessor.setUser(new UsernamePasswordAuthenticationToken(user,""));
                }
                return message;
            }
			
		});
	}
}
