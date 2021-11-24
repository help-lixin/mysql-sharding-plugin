package help.lixin.datasource.mybatis.scripting.xmltags;

import help.lixin.resource.bindings.IBindingsVariable;
import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.apache.ibatis.scripting.ScriptingException;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.OgnlCache;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.type.SimpleTypeRegistry;

import java.util.ServiceLoader;
import java.util.regex.Pattern;

public class TextSqlNode implements SqlNode {
    private final String text;
    private final Pattern injectionFilter;

    public TextSqlNode(String text) {
        this(text, (Pattern) null);
    }

    public TextSqlNode(String text, Pattern injectionFilter) {
        this.text = text;
        this.injectionFilter = injectionFilter;
    }

    public boolean isDynamic() {
        TextSqlNode.DynamicCheckerTokenParser checker = new TextSqlNode.DynamicCheckerTokenParser();
        GenericTokenParser parser = this.createParser(checker);
        parser.parse(this.text);
        return checker.isDynamic();
    }

    public boolean apply(DynamicContext context) {
        GenericTokenParser parser = this.createParser(new TextSqlNode.BindingTokenParser(context, this.injectionFilter));
        context.appendSql(parser.parse(this.text));
        return true;
    }

    private GenericTokenParser createParser(TokenHandler handler) {
        return new GenericTokenParser("${", "}", handler);
    }

    private static class DynamicCheckerTokenParser implements TokenHandler {
        private boolean isDynamic;

        public DynamicCheckerTokenParser() {
        }

        public boolean isDynamic() {
            return this.isDynamic;
        }

        public String handleToken(String content) {
            this.isDynamic = true;
            return null;
        }
    }

    private static class BindingTokenParser implements TokenHandler {
        private DynamicContext context;
        private Pattern injectionFilter;

        public BindingTokenParser(DynamicContext context, Pattern injectionFilter) {
            this.context = context;
            this.injectionFilter = injectionFilter;
        }

        public String handleToken(String content) {
            Object parameter = this.context.getBindings().get("_parameter");
            if (parameter == null) {
                this.context.getBindings().put("value", (Object) null);
            } else if (SimpleTypeRegistry.isSimpleType(parameter.getClass())) {
                this.context.getBindings().put("value", parameter);
            }

            // 通过SPI进行处理.
            ServiceLoader<IBindingsVariable> bindingsVariables = ServiceLoader.load(IBindingsVariable.class);
            bindingsVariables.forEach(bindingsVariable -> {
                bindingsVariable.bindings(context.getBindings());
            });


            Object value = OgnlCache.getValue(content, this.context.getBindings());
            String srtValue = value == null ? "" : String.valueOf(value);
            this.checkInjection(srtValue);
            return srtValue;
        }

        private void checkInjection(String value) {
            if (this.injectionFilter != null && !this.injectionFilter.matcher(value).matches()) {
                throw new ScriptingException("Invalid input. Please conform to regex" + this.injectionFilter.pattern());
            }
        }
    }
}
