/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * 注册服务
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-9-19
 */
@Configuration
@EnableWebMvc
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topicChat","/userChat");//这句表示在topicChat和userChat这两个域上可以向客户端发消息；  
        config.setApplicationDestinationPrefixes("/appChat");//这句表示客户端向服务端发送时的主题上面需要加"/appChat"作为前缀  
        config.setUserDestinationPrefix("/userChat/");//这句表示给指定用户发送(一对一)的主题前缀是“/userChat/”
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//添加这个Endpoint,这样在网页中就可以通过webSckServer连接上服务了
		registry.addEndpoint("/webSckServer").withSockJS();
	}
	
}
