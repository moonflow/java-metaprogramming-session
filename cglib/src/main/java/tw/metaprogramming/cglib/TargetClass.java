package tw.metaprogramming.cglib;

public class TargetClass implements ProxyInterface{
    public void ProxyMethod(String desc) {
        System.out.println("TargetClass 调用 ProxyMethod desc=" + desc);
    }
}
