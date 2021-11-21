package help.lixin.datasource.mybatis;

import help.lixin.datasource.mybatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;

public class LanguageDriverCustomizer implements ConfigurationCustomizer {

    @Override
    public void customize(Configuration configuration) {
        configuration.setDefaultScriptingLanguage(XMLLanguageDriver.class);
    }
}
