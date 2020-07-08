package tw.metaprogramming.triangle.annotation;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Utils {
    public static void doCry(List<Object> animalList) {
        System.out.println("注解模拟多态");
        animalList.forEach(animal -> {
            Arrays.stream(animal.getClass().getMethods())
                .forEach(cryMethod -> {
                    Cry cryAnnotation = cryMethod.getAnnotation(Cry.class);
                    if(!Objects.isNull(cryAnnotation)) {
                        try {
                            cryMethod.invoke(animal);
                        } catch (Exception e) {
                        }
                    }
                });
        });
    }
}
