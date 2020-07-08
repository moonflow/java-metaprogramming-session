package tw.metaprogramming.permission.permission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;

@Aspect
@Component
public class PermissionAspect {
    @Around("@annotation(tw.metaprogramming.permission.permission.Permission)")
    public Object checkFuncPermissions(ProceedingJoinPoint point) throws Throwable{
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Permission permission = AnnotationUtils.getAnnotation(methodSignature.getMethod(), Permission.class);

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(new PermissionInterfaceImpl());
        Parameter[] paras = methodSignature.getMethod().getParameters();
        Object[] args = point.getArgs();

        for(int i = 0; i < paras.length; ++i) {
            context.setVariable(paras[i].getName(), args[i]);
        }

        boolean isPermit = parser.parseExpression(permission.value()).getValue(context, Boolean.class);
        if(!isPermit) {
            throw new RuntimeException("Permission Denied");
        }
        return point.proceed();
    }
}
