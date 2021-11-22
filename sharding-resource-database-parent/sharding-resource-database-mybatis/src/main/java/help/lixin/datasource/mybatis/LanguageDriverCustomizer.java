package help.lixin.datasource.mybatis;

import help.lixin.datasource.mybatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

public class LanguageDriverCustomizer implements ConfigurationCustomizer {

    private Logger logger = LoggerFactory.getLogger(LanguageDriverCustomizer.class);

    @Override
    public void customize(Configuration configuration) {
        // 添加自定义的:LanguageDriver实现
        configuration.setDefaultScriptingLanguage(XMLLanguageDriver.class);

        try {
            // LanguageDriverRegistry对象,居然不支持数据的删除功能.只能通过反射去做了,仅幸的是这段代码仅仅是在启动应用时,调用一次
            Field field = configuration.getLanguageRegistry().getClass().getDeclaredField("LANGUAGE_DRIVER_MAP");
            field.setAccessible(true);
            // 删除LanguageDriverRegistry对象对象中默认的:org.apache.ibatis.scripting.xmltags.XMLLanguageDriver
            Map languageDriverMap = (Map) field.get(configuration.getLanguageRegistry());
            Class clazz = org.apache.ibatis.scripting.xmltags.XMLLanguageDriver.class;
            if (languageDriverMap.containsKey(clazz)) {
                languageDriverMap.remove(clazz);
            }

            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("配置Configuration出现了错误,错误信息:[{}]", e);
        }
    }
}
