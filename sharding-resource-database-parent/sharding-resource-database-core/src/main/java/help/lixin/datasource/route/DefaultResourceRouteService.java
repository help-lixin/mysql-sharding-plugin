package help.lixin.datasource.route;

import help.lixin.datasource.context.DBResourceContext;
import help.lixin.resource.context.ResourceContext;
import help.lixin.resource.route.IResourceContextCustomizer;
import help.lixin.resource.route.IResourceRouteService;
import help.lixin.resource.route.contxt.InvokeContext;

import java.util.List;

public class DefaultResourceRouteService implements IResourceRouteService {

    private List<IResourceContextCustomizer> resourceContextCustomizer;

    public DefaultResourceRouteService(List<IResourceContextCustomizer> resourceContextCustomizer) {
        this.resourceContextCustomizer = resourceContextCustomizer;
    }

    @Override
    public ResourceContext route(InvokeContext ctx) {
        // 1. 构建上下文的build
        DBResourceContext.Build contextBuild = DBResourceContext.newBuild();
        // 2. build的详细信息,委托给:IResourceContextCustomizer
        if (null != resourceContextCustomizer) {
            resourceContextCustomizer.forEach(item -> {
                item.apply(ctx, contextBuild);
            });
        }
        // 3. 最终build
        return contextBuild.build();
    }
}
