package tw.metaprogramming.bytecodeenhancement.entity;

import tw.metaprogramming.bytecodeenhancement.Getter;
import tw.metaprogramming.bytecodeenhancement.Setter;

public class TestEntity {
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String name;
}
