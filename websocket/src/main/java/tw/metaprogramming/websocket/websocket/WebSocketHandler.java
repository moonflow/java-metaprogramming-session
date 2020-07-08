package tw.metaprogramming.websocket.websocket;

import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WebSocketHandler {
    final private Object object;
    final private Method method;
    @Getter
    final private Class<?> dataType;
    public WebSocketHandler(Object object, Method method) {
        this.object = object;
        this.method = method;
        this.dataType = method.getParameterTypes()[1];
    }

    public Object invoke(WebSocketSession session, Object msg) throws
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return method.invoke(object, session, msg);
    }
}
