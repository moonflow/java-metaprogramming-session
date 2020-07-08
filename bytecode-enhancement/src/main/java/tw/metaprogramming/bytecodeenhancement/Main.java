package tw.metaprogramming.bytecodeenhancement;

import javassist.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class Main {
    public static HashMap<String, Class<?>> classMap = new HashMap<>();

    public static void main(String[] args) throws
            IOException, NotFoundException, CannotCompileException, ClassNotFoundException, SecurityException,
            InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        bytecodeEnhance();

        runTest();
    }

    public static void runTest() throws
            ClassNotFoundException, InstantiationException, IllegalAccessException,
            NoSuchMethodException, SecurityException, IllegalArgumentException,
            InvocationTargetException{
        Class<?> testAEntityClass = Class.forName("tw.metaprogramming.bytecodeenhancement.entity.TestEntity");
        Object testAEntityIns = testAEntityClass.newInstance();
        Method testAEntitySetIdMethod = testAEntityClass.getMethod("setId", String.class);
        testAEntitySetIdMethod.invoke(testAEntityIns, "AId");
        Method testAEntityGetIdMethod = testAEntityClass.getMethod("getId");
        Object getResult = testAEntityGetIdMethod.invoke(testAEntityIns);
        if(getResult.equals("AId")) {
            System.out.println("success");
        } else {
            System.out.println("fail");
        }
    }

    public static void bytecodeEnhance() throws
            IOException, NotFoundException, ClassNotFoundException, CannotCompileException{
        Set<String> classNameSet =  PackageScanner.getAllClassesName("tw.metaprogramming.bytecodeenhancement.entity");

        //CtClass 的容器
        ClassPool classPool = ClassPool.getDefault();
        for(String className : classNameSet) {
            //对类的抽象
            CtClass ctClass = classPool.get(className);
            TryAddGetterAndSetter(className, ctClass);
        }
    }

    public static void TryAddGetterAndSetter(String className, CtClass ctClass) throws ClassNotFoundException, CannotCompileException {
        //对类成员的抽象
        for(CtField ctField : ctClass.getDeclaredFields()) {
            TryAddGetter(ctClass, ctField);
            TryAddSetter(ctClass, ctField);
        }
        Class enhanceClass = ctClass.toClass();
        classMap.put(className, enhanceClass);
    }

    public static void TryAddSetter(CtClass ctClass, CtField ctField) throws ClassNotFoundException, CannotCompileException {
        Object setterObject = ctField.getAnnotation(Setter.class);
        if(Objects.isNull(setterObject) || !(setterObject instanceof Setter)) {
            return;
        }
        Setter setter = (Setter)setterObject;
        ctClass.addMethod(CtNewMethod.setter(toSetMethodName(ctField.getName()), ctField));
    }

    public static void TryAddGetter(CtClass ctClass, CtField ctField) throws ClassNotFoundException, CannotCompileException {
        Object getterObject = ctField.getAnnotation(Getter.class);
        if(Objects.isNull(getterObject) || !(getterObject instanceof Getter)) {
            return;
        }
        Getter getter = (Getter)getterObject;
        ctClass.addMethod(CtNewMethod.getter(toGetMethodName(ctField.getName()), ctField));
    }

    public static String toGetMethodName(String field) {
        return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    public static String toSetMethodName(String field) {
        return "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }
}
