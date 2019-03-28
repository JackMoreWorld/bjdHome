package com.bgd.app.util.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Bean
	public ServerEndpointExporter serverEndpointExporter(ApplicationContext context) {
		return new ServerEndpointExporter();
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(noticeHandler(), "/bgd/notice")
                .addInterceptors(new HttpSessionHandshakeInterceptor()).setAllowedOrigins("*");
	}
	
	@Bean
	public NoticeHandler noticeHandler(){
		return  NoticeHandler.getInstance();
	}

}
