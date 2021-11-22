package help.lixin.datasource.mybatis.scripting.xmltags;

import help.lixin.datasource.context.DBResourceContextInfo;
import help.lixin.resource.context.ResourceContext;
import help.lixin.resource.context.ResourceContextInfo;
import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.apache.ibatis.scripting.ScriptingException;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.OgnlCache;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.type.SimpleTypeRegistry;

import java.util.Map;
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

            // 增加从线程上下文获得变量信息
            // lixin 2021/11/21
            ResourceContextInfo tmp = ResourceContext.get();
            if (null != tmp && tmp instanceof DBResourceContextInfo
                    && !context.getBindings().containsKey("database")
                    && !context.getBindings().containsKey("tablePrefix")
            ) {
                DBResourceContextInfo ctx = (DBResourceContextInfo) tmp;
                String tenantId = ctx.getTenantId();
                String database = ctx.getDatabase();
                String tablePrefix = ctx.getTablePrefix();
                String microServiceName = ctx.getMicroServiceName();
                Map<String, String> properties = ctx.getProperties();

                context.getBindings().put("tenantId", tenantId);
                context.getBindings().put("database", database);
                context.getBindings().put("tablePrefix", tablePrefix);
                context.getBindings().put("microServiceName", microServiceName);

                properties.forEach((key, value) -> {
                    context.getBindings().put(key, value);
                });
            }

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
