package help.lixin.resource.route;

import help.lixin.resource.context.AbstractResourceContext;
import help.lixin.resource.route.contxt.InvokeContext;

public interface IResourceContextCustomizer {

    void apply(InvokeContext ctx, AbstractResourceContext.Build ctxBuild);
}
