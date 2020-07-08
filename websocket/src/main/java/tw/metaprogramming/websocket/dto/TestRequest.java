package tw.metaprogramming.websocket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestRequest {
    private long id;
    private String command;
}
