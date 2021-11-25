package help.lixin.datasource.service.loadbalancer;

import help.lixin.datasource.context.DBResourceContext;

import javax.sql.DataSource;
import java.util.Optional;

public interface IRuleService {

    Optional<DataSource> choose(DBResourceContext ctx);
}
