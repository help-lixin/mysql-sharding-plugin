package help.lixin.resource.route;

import help.lixin.resource.context.ResourceContextInfo;
import help.lixin.resource.route.contxt.IResourceContext;

public interface IResourceRouteService {
    
    ResourceContextInfo route(IResourceContext ctx);
}
