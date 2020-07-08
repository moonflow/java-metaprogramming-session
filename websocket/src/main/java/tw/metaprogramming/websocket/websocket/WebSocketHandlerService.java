package tw.metaprogramming.websocket.websocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Objects;

@Log4j2
@Service
public class WebSocketHandlerService extends AbstractWebSocketHandler {

    private final ObjectMapper objectMapper;

    private final WebSocketMessageMappingService webSocketMessageMappingService;

    public WebSocketHandlerService(WebSocketMessageMappingService webSocketMessageMappingService) {
        this.objectMapper = new ObjectMapper();
        this.webSocketMessageMappingService = webSocketMessageMappingService;

        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            Request request = this.objectMapper.readValue(message.getPayload(), Request.class);
            if(Strings.isEmpty(request.getKey())) {
                return;
            }
            WebSocketHandler webSocketHandler =
                    webSocketMessageMappingService.getWebSocketHandler(request.getKey());
            if(Objects.isNull(webSocketHandler)) {
                return;
            }
            Object data = this.objectMapper.readValue(request.getData(), webSocketHandler.getDataType());
            Object responseData = webSocketHandler.invoke(session, data);
            Response response = new Response(request.getKey(), responseData);
            TextMessage textMessage = new TextMessage(this.objectMapper.writeValueAsString(response));
            session.sendMessage(textMessage);
        } catch (Exception e) {
            log.error("websocket msg handler error {}", message, e);
        }
    }
}
