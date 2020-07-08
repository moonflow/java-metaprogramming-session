package tw.metaprogramming.triangle;

import tw.metaprogramming.triangle.normal.Animal;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Animal> normalList = Arrays.asList(new tw.metaprogramming.triangle.normal.Cat(),
                new tw.metaprogramming.triangle.normal.Dog());

        List<Object> annotationList = Arrays.asList(new tw.metaprogramming.triangle.annotation.Cat(),
                new tw.metaprogramming.triangle.annotation.Dog());

        tw.metaprogramming.triangle.normal.Utils.doCry(normalList);

        tw.metaprogramming.triangle.annotation.Utils.doCry(annotationList);
    }
}
