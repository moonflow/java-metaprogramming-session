package tw.metaprogramming.annotationprocessing.example;

public class Main {
    public static void main(String[] args) {
        TestEntity testEntity = new TestEntity();
        testEntity.setId("testId");
        String id = testEntity.getId();
        System.out.println(id);
    }
}
