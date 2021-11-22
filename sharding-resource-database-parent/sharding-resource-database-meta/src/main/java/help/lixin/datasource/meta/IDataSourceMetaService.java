package help.lixin.datasource.meta;

import help.lixin.datasource.model.DatabaseResource;
import help.lixin.resource.listener.IEventListener;

import java.util.List;

/**
 * 解析元数据
 */
public interface IDataSourceMetaService extends IEventListener {

    /**
     * 解析元数据
     *
     * @return
     */
    List<DatabaseResource> getMeta();
}
