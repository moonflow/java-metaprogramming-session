package tw.metaprogramming.triangle.annotation;

public class Dog {
    @Cry
    public void bark() {
        System.out.println("狗汪汪");
    }

    public void eat() {
        System.out.println("狗吃骨头");
    }
}
