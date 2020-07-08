package tw.metaprogramming.dynamicproxy;

public class TargetClass implements ProxyInterface{
    public void ProxyMethod(String desc) {
        System.out.println("TargetClass 调用 ProxyMethod desc=" + desc);
    }
}
