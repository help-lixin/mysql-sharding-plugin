package help.lixin.datasource.build.context;

import help.lixin.datasource.context.DBResourceContext;
import help.lixin.resource.context.ResourceContext;
import help.lixin.resource.route.IResourceContextCustomizer;
import help.lixin.resource.route.IBuildResourceContextService;
import help.lixin.resource.route.contxt.Invocation;

import java.util.List;

public class DefaultBuildResourceContextService implements IBuildResourceContextService {

    private List<IResourceContextCustomizer> resourceContextCustomizer;

    public DefaultBuildResourceContextService(List<IResourceContextCustomizer> resourceContextCustomizer) {
        this.resourceContextCustomizer = resourceContextCustomizer;
    }

    @Override
    public ResourceContext build(Invocation invocation) {
        // 1. 构建上下文的build
        DBResourceContext.Build contextBuild = DBResourceContext.newBuild();
        // 2. build的详细信息,委托给:IResourceContextCustomizer
        if (null != resourceContextCustomizer) {
            resourceContextCustomizer.forEach(item -> {
                item.apply(invocation, contextBuild);
            });
        }
        // 3. 最终build
        return contextBuild.build();
    }
}
