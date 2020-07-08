package tw.metaprogramming.websocket.websocket;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class WebSocketMessageMappingService implements BeanPostProcessor {
    private Map<String, WebSocketHandler> messageMappingContainer = new HashMap<>();

    public WebSocketHandler getWebSocketHandler(String key) {
        return messageMappingContainer.get(key);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        WsController wsController = beanClass.getAnnotation(WsController.class);
        if(Objects.isNull(wsController)) {
            return bean;
        }
        for(Method method : beanClass.getMethods()) {
            WsMessageMapping wsMessageMapping = AnnotationUtils.findAnnotation(method, WsMessageMapping.class);
            if(Objects.isNull(wsMessageMapping) || wsMessageMapping.key().isEmpty()) {
                continue;
            }
            this.messageMappingContainer.put(wsMessageMapping.key(), new WebSocketHandler(bean, method));
        }
        return bean;
    }
}
