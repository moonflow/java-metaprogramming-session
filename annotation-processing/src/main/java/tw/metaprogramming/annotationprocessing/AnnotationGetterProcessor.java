package tw.metaprogramming.annotationprocessing;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
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
public class AnnotationGetterProcessor extends AbstractProcessor {
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
        return Stream.of(Getter.class.getCanonicalName()).collect(Collectors.toSet());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //获取所有被Getter注解标注的字段
        Set<? extends Element> annotation = roundEnv.getElementsAnnotatedWith(Getter.class);
        annotation.stream().forEach(element -> trees.getTree(element).accept(new TreeTranslator() {
            @Override
            public void visitVarDef(JCTree.JCVariableDecl jcVariable) {
                //获得字段父节点，即类节点
                super.visitVarDef(jcVariable);
                JCTree.JCClassDecl jcClass = (JCTree.JCClassDecl)(trees.getPath(element).getParentPath().getLeaf());
                //定义get方法
                jcClass.defs = jcClass.defs.prepend(generateGetterMethod(jcVariable));
                messager.printMessage(Diagnostic.Kind.NOTE,
                        String.format("为类\"%s\"的字段\"%s\"添加get方法", jcClass.getSimpleName(), jcVariable.getName()));
            }
        }));
        return true;
    }

    private JCTree.JCMethodDecl generateGetterMethod(JCTree.JCVariableDecl jcVariable) {

        //修改方法级别
        JCTree.JCModifiers jcModifiers = treeMaker.Modifiers(Flags.PUBLIC);

        //添加方法名称
        com.sun.tools.javac.util.Name methodName = getMethodSignature(jcVariable.getName());

        //添加方法内容
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        jcStatements.append(
                treeMaker.Return(treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariable.getName())));
        JCTree.JCBlock jcBlock = treeMaker.Block(0, jcStatements.toList());

        //添加返回值类型
        JCTree.JCExpression returnType = jcVariable.vartype;

        //参数类型
        List<JCTree.JCTypeParameter> typeParameters = List.nil();

        //参数变量
        List<JCTree.JCVariableDecl> parameters = List.nil();

        //声明异常
        List<JCTree.JCExpression> throwsClauses = List.nil();
        //构建方法
        return treeMaker
                .MethodDef(jcModifiers, methodName, returnType, typeParameters, parameters, throwsClauses, jcBlock, null);
    }

    private Name getMethodSignature(Name name) {
        String methodStr = "get" + name.toString().substring(0, 1).toUpperCase() + name.toString().substring(1);
        return names.fromString(methodStr);
    }
}
