package tw.metaprogramming.annotationprocessing.example;

import tw.metaprogramming.annotationprocessing.Getter;
import tw.metaprogramming.annotationprocessing.Setter;

public class TestEntity {
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String name;
}
