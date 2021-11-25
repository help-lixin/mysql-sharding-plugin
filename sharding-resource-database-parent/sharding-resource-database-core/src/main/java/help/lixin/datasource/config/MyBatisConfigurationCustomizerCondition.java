package help.lixin.datasource.config;

import help.lixin.datasource.OverrideSQLMode;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MyBatisConfigurationCustomizerCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String overrideMode = context.getEnvironment().getProperty("spring.sharding.resource.overrideMode", OverrideSQLMode.MYBATIS.name());
        if (overrideMode.equalsIgnoreCase(OverrideSQLMode.MYBATIS.name())) {
            return true;
        }
        return false;
    }
}