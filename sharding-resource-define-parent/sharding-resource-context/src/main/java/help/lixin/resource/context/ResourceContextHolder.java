package help.lixin.resource.context;

/**
 * 资源上下文
 */
public abstract class ResourceContextHolder {
    private static final ThreadLocal<ResourceContext> CTX = new InheritableThreadLocal<>();

    public static void bind(ResourceContext context) {
        CTX.set(context);
    }

    public static ResourceContext get() {
        return CTX.get();
    }

    public static void unBind() {
        CTX.remove();
    }
}
