package help.lixin.datasource.mybatis.condition;

import help.lixin.datasource.RewriteSQLMode;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MyBatisConfigurationCustomizerCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String overrideMode = context.getEnvironment().getProperty("spring.sharding.resource.rewriteMode", RewriteSQLMode.MYBATIS.name());
        if (overrideMode.equalsIgnoreCase(RewriteSQLMode.MYBATIS.name())) {
            return true;
        }
        return false;
    }
}