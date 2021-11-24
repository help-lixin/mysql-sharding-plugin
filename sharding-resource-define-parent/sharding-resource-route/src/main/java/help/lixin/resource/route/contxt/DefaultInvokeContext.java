package help.lixin.resource.route.contxt;

import java.lang.reflect.Method;
import java.util.Map;

public class DefaultInvokeContext implements InvokeContext {
    private Method method;
    private Class<?> clazz;
    private Object instance;
    private Map<String, Object> properties;

    public static Build newBuild() {
        return new Build();
    }

    @Override
    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public void setInstance(Object instance) {
        this.instance = instance;
    }

    @Override
    public Object getInstance() {
        return instance;
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    public static class Build {
        private Method method;
        private Class<?> clazz;
        private Object instance;
        private Map<String, Object> properties;

        public Build method(Method method) {
            this.method = method;
            return this;
        }

        public Build clazz(Class<?> clazz) {
            this.clazz = clazz;
            return this;
        }

        public Build instance(Object instance) {
            this.instance = instance;
            return this;
        }

        public Build properties(Map<String, Object> properties) {
            if (null != properties) {
                this.properties = properties;
            }
            return this;
        }

        public Build properties(String key, Object value) {
            this.properties.put(key, value);
            return this;
        }

        public InvokeContext build() {
            InvokeContext ctx = new DefaultInvokeContext();
            ctx.setMethod(this.method);
            ctx.setClazz(this.clazz);
            ctx.setInstance(this.instance);
            ctx.setProperties(this.properties);
            return ctx;
        }
    }
}
