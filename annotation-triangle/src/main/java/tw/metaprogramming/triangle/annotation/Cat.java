package tw.metaprogramming.triangle.annotation;

public class Cat {
    @Cry
    public void meow() {
        System.out.println("猫喵喵");
    }

    public void eat() {
        System.out.println("猫吃鱼");
    }
}
