package help.lixin.resource.route;

import help.lixin.resource.context.ResourceContext;
import help.lixin.resource.route.contxt.IResourceContext;

public interface IResourceRouteService {
    
    ResourceContext route(IResourceContext ctx);
}
