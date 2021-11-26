package help.lixin.datasource.config;

import help.lixin.resource.event.Event;
import help.lixin.resource.listener.IEventListener;
import help.lixin.resource.publisher.DefaultEventPublisher;
import help.lixin.resource.publisher.IEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;

@Configuration
public class CommonConfig {
    private Logger logger = LoggerFactory.getLogger(ShardingResourceAopConfig.class);

    /**
     * 事件发布者
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public IEventPublisher eventPublisher() {
        return new DefaultEventPublisher();
    }

    /**
     * 事件监听者
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "loggerEventListener")
    public IEventListener loggerEventListener() {
        IEventListener listener = new IEventListener() {
            @Override
            public void onEvent(Event event) {
                logger.info("trace event [{}]", event);
            }
        };
        return listener;
    }


    /**
     * 配置监听者与发布者的关系
     */
    @Configuration
    public class ConfigurationListener {
        @Autowired
        private IEventPublisher eventPublisher;

        @Autowired(required = false)
        private Optional<List<IEventListener>> listeners = Optional.empty();

        @PostConstruct
        public void init() {
            listeners.ifPresent(consumer -> {
                for (IEventListener listener : consumer) {
                    eventPublisher.subscribe(listener);
                }
            });

        }

        @PreDestroy
        public void destory() {
            listeners.ifPresent(consumer -> {
                for (IEventListener listener : consumer) {
                    eventPublisher.unSubscribe(listener);
                }
            });
        }
    }

}
