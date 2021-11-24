package help.lixin.resource.route;

import help.lixin.resource.context.AbstractResourceContext;
import help.lixin.resource.route.contxt.Invocation;

public interface IResourceContextCustomizer {

    void apply(Invocation ctx, AbstractResourceContext.Build ctxBuild);
}
