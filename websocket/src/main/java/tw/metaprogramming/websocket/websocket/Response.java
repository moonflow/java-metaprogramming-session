package tw.metaprogramming.websocket.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Response {
    private String key;
    private Object data;
}
