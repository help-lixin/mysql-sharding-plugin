package help.lixin.resource.route;

import help.lixin.resource.context.ResourceContext;
import help.lixin.resource.route.contxt.Invocation;

/**
 * 构建ResourceContext服务
 */
public interface IBuildResourceContextService {

    ResourceContext build(Invocation invocation);
}
