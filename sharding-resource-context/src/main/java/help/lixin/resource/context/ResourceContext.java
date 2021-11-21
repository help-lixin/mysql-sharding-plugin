package help.lixin.resource.context;

/**
 * 资源上下文
 */
public abstract class ResourceContext {
    private static final ThreadLocal<ResourceContextInfo> CTX = new InheritableThreadLocal<>();

    public static void bind(ResourceContextInfo context) {
        CTX.set(context);
    }

    public static ResourceContextInfo get() {
        return CTX.get();
    }

    public static void unBind(ResourceContextInfo context) {
        CTX.remove();
    }
}
