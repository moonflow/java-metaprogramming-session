package tw.metaprogramming.dynamicproxy;

import java.lang.reflect.InvocationHandler;

public class TargetClassDynamicProxy implements ProxyInterface{
    InvocationHandler invocationHandler;
    public TargetClassDynamicProxy(InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }
    public void ProxyMethod(String desc) {
        try {
            invocationHandler.invoke(this,
                    ProxyInterface.class.getMethod("ProxyMethod", String.class),
                    new Object[] {desc});
        } catch (Throwable e){

        }
    }
}
