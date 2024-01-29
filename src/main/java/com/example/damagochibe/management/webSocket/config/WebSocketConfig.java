    package com.example.damagochibe.management.webSocket.config;

    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.messaging.simp.SimpMessagingTemplate;
    import org.springframework.messaging.simp.config.MessageBrokerRegistry;
    import org.springframework.web.socket.WebSocketHandler;
    import org.springframework.web.socket.config.annotation.*;
    import org.springframework.web.socket.server.standard.ServerEndpointExporter;

    @Configuration
    @EnableWebSocketMessageBroker
    public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
        @Override
        public void configureMessageBroker(MessageBrokerRegistry registry) {
            registry.enableSimpleBroker("/topic", "/queue"); // topic 모든 클라이언트, queue 특정 클라이언트
            registry.setApplicationDestinationPrefixes("/app"); // /app 경로로 메시지를 보내면 @MessageMapping 어노테이션이 붙은곳으로 간다
        }

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
                registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:5000")
                    .withSockJS();
            registry.addEndpoint("/ma").setAllowedOrigins("http://localhost:5000")
                    .withSockJS();
                registry.addEndpoint("/battle").setAllowedOrigins("http://localhost:5000").withSockJS();
        }

    }