package tw.metaprogramming.annotationprocessing;

import com.google.auto.service.AutoService;
import com.google.common.base.Throwables;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AutoService(Processor.class)
public class AnnotationSetterProcessor extends AbstractProcessor {
    /**
     * 抽象语法树
     */
    private JavacTrees trees;

    /**
     * AST
     */
    private TreeMaker treeMaker;

    /**
     * 标识符
     */
    private Names names;

    /**
     * 日志处理
     */
    private Messager messager;

    /**
     * 过滤器
     */
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment)processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        messager = processingEnv.getMessager();
        this.names = Names.instance(context);
        filer = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Stream.of(Setter.class.getCanonicalName()).collect(Collectors.toSet());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //获取所有被Getter注解标注的字段
        Set<? extends Element> annotation = roundEnv.getElementsAnnotatedWith(Setter.class);
        annotation.stream().forEach(element -> trees.getTree(element).accept(new TreeTranslator() {
            @Override
            public void visitVarDef(JCTree.JCVariableDecl jcVariable) {
                //获得字段父节点，即类节点
                super.visitVarDef(jcVariable);
                JCTree.JCClassDecl jcClass = (JCTree.JCClassDecl)(trees.getPath(element).getParentPath().getLeaf());
                try {
                    //定义set方法
                    jcClass.defs = jcClass.defs.prepend(generateSetterMethod(jcVariable));
                } catch (Exception e) {
                    messager.printMessage(Diagnostic.Kind.ERROR, Throwables.getStackTraceAsString(e));
                }
                messager.printMessage(Diagnostic.Kind.NOTE,
                        String.format("为类\"%s\"的字段\"%s\"添加set方法", jcClass.getSimpleName(), jcVariable.getName()));
            }
        }));
        return true;
    }

    private JCTree.JCMethodDecl generateSetterMethod(JCTree.JCVariableDecl jcVariable) throws ReflectiveOperationException{

        //修改方法级别
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);

        //添加方法名称
        Name variableName = jcVariable.getName();
        Name methodName = setMethodSignature(variableName);

        //设置方法体
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        jcStatements.append(treeMaker.Exec(treeMaker
                .Assign(treeMaker.Select(treeMaker.Ident(names.fromString("this")), variableName),
                        treeMaker.Ident(variableName))));
        //定义方法体
        JCTree.JCBlock jcBlock = treeMaker.Block(0, jcStatements.toList());

        //添加返回值类型
        JCTree.JCExpression returnType =
                treeMaker.Type((Type) (Class.forName("com.sun.tools.javac.code.Type$JCVoidType").newInstance()));

        List<JCTree.JCTypeParameter> typeParameters = List.nil();

        //定义参数
        JCTree.JCVariableDecl variableDecl = treeMaker
                .VarDef(treeMaker.Modifiers(Flags.PARAMETER, List.nil()), jcVariable.name, jcVariable.vartype, null);
        List<JCTree.JCVariableDecl> parameters = List.of(variableDecl);

        //声明异常
        List<JCTree.JCExpression> throwsClauses = List.nil();
        return treeMaker
                .MethodDef(modifiers, methodName, returnType, typeParameters, parameters, throwsClauses, jcBlock, null);
    }

    private Name setMethodSignature(Name name) {
        String methodStr = "set" + name.toString().substring(0, 1).toUpperCase() + name.toString().substring(1);
        return names.fromString(methodStr);
    }
}
