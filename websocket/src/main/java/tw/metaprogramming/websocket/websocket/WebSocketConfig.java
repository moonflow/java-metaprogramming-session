package tw.metaprogramming.websocket.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandlerService webSocketHandlerService;

    public WebSocketConfig(WebSocketHandlerService webSocketHandlerService) {
        this.webSocketHandlerService = webSocketHandlerService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this.webSocketHandlerService, "/websocket").setAllowedOrigins("*");
    }
}
