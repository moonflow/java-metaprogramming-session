package tw.metaprogramming.triangle.normal;

import java.util.List;

public class Utils {
    public static void doCry(List<Animal> animalList) {
        System.out.println("Java多态:");
        animalList.forEach(Animal::cry);
    }
}
