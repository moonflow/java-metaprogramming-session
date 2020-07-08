package tw.metaprogramming.websocket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TestResponse {
    private long id;
    private String result;
}
