package tw.metaprogramming.websocket.websocket;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {
    private String key;
    private String data;

    @JsonSetter("data")
    public void setData(JsonNode data) {
        this.data = data.toPrettyString();
    }
}
