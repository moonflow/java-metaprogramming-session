package tw.metaprogramming.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class ProxyHandler implements MethodInterceptor {
    @Override
    public Object intercept(Object object, /*增强后的对象*/
                            Method method, /*拦截的方法*/
                            Object[] args, /*调用参数列表*/
                            MethodProxy proxy) /*代理处理对象*/
            throws Throwable {
        try {
            System.out.println(method.getName() + "前置通知");
            Object result = proxy.invokeSuper(object, args);
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
