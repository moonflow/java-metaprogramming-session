package tw.metaprogramming.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ProxyHandler implements InvocationHandler {
    private Object target;
    public ProxyHandler(Object target) {
        this.target = target;
    }
    public Object invoke(Object proxy, /*增强后的对象*/
                         Method method, /*拦截的方法*/
                         Object[] args /*调用参数列表*/) throws Throwable {
        boolean isInterfaceMethod = Arrays.stream(ProxyInterface.class.getMethods())
                .anyMatch(interfaceMethod -> interfaceMethod.equals(method));
        if(!isInterfaceMethod) {
            return null;
        }
        try {
            System.out.println(method.getName() + "前置通知");
            Object result = method.invoke(target, args);
            System.out.println(method.getName() + "后置通知");
            return result;
        } catch (Exception e) {
            System.out.println(method.getName() + "异常通知");
            throw e;
        } finally {
            System.out.println(method.getName() + "最终通知");
        }
    }
}
