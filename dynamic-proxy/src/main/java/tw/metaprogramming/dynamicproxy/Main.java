package tw.metaprogramming.dynamicproxy;

import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {
        TargetClass targetInstance = new TargetClass();
        ProxyHandler proxyHandler = new ProxyHandler(targetInstance);
        Class<?>[] interfaces = {ProxyInterface.class};
        //原生动态代理
        System.out.println("原生动态代理");
        ProxyInterface proxyInstance = (ProxyInterface) Proxy.newProxyInstance(targetInstance.getClass().getClassLoader(),
                interfaces,
                proxyHandler);
        proxyInstance.ProxyMethod("test");

        //模拟原生动态代理
        System.out.println("模拟原生动态代理");
        TargetClassDynamicProxy targetClassDynamicProxy = new TargetClassDynamicProxy(proxyHandler);
        targetClassDynamicProxy.ProxyMethod("test");
    }
}
