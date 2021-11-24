package help.lixin.datasource.build.context.customizer;

import help.lixin.datasource.context.DBResourceContext;
import help.lixin.resource.context.AbstractResourceContext;
import help.lixin.resource.route.IResourceContextCustomizer;
import help.lixin.resource.route.contxt.Invocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * 解析注解:@Transactional看注解上是否配置了为只读模式,如果是只读模式,则设置为只读模式,在只读模式下,将会选择只读的数据源来着.
 */
public class TransactionalResourceContextCustomizer implements IResourceContextCustomizer {

    @Override
    public void apply(Invocation invocation, AbstractResourceContext.Build ctxBuild) {
        if (!(ctxBuild instanceof DBResourceContext.Build)) {
            return;
        }
        
        // 强制转换为:DBResourceContext.Build
        DBResourceContext.Build build = (DBResourceContext.Build) ctxBuild;
        boolean aopProxy = AopUtils.isAopProxy(invocation.getInstance());
        Class<?> targetClass = invocation.getClazz();
        if (aopProxy) {
            // 获得被AOP之后的真实对象
            targetClass = AopUtils.getTargetClass(invocation.getInstance());
        }

        // 1. 先查询方法是否有注解(@Transactional)
        Transactional annotation = AnnotationUtils.findAnnotation(invocation.getMethod(), Transactional.class);
        if (null == annotation) {
            // 2.方法上没有注解,可能类上有注解@Transactional
            annotation = AnnotationUtils.findAnnotation(targetClass, Transactional.class);
        }

        if (null != annotation) {
            // 设置为只读模式
            build.readOnly(annotation.readOnly());
        }
    }
}
