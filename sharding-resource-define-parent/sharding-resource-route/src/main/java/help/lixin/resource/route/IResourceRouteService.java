package help.lixin.resource.route;

import help.lixin.resource.context.ResourceContext;
import help.lixin.resource.route.contxt.InvokeContext;

public interface IResourceRouteService {
    
    ResourceContext route(InvokeContext ctx);
}
