package tw.metaprogramming.websocket.controller;

import org.springframework.web.socket.WebSocketSession;
import tw.metaprogramming.websocket.dto.TestRequest;
import tw.metaprogramming.websocket.dto.TestResponse;
import tw.metaprogramming.websocket.websocket.WsController;
import tw.metaprogramming.websocket.websocket.WsMessageMapping;

@WsController
public class TestWsController {
    @WsMessageMapping("test1")
    public TestResponse test1(WebSocketSession session, TestRequest request) {
        System.out.println("recv request " + request);
        return TestResponse.builder()
                .id(request.getId())
                .result("test1 " + request.getCommand() + " processed")
                .build();
    }

    @WsMessageMapping(key = "test2")
    public TestResponse test2(WebSocketSession session, TestRequest request) {
        System.out.println("recv request " + request);
        return TestResponse.builder()
                .id(request.getId())
                .result("test2 " + request.getCommand() + " processed")
                .build();
    }
}
