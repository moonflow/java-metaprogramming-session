package tw.metaprogramming.cglib;

import net.sf.cglib.proxy.Enhancer;

public class Main {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        //设置需要代理的类
        enhancer.setSuperclass(TargetClass.class);
        //设置代理中介
        enhancer.setCallback(new ProxyHandler());
        //创建代理实例
        TargetClass proxyInstance = (TargetClass)enhancer.create();
        proxyInstance.ProxyMethod("test");
    }
}
