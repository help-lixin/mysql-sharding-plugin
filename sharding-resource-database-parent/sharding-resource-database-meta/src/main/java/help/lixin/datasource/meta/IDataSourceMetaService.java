package help.lixin.datasource.meta;

import help.lixin.datasource.model.DatabaseResource;

import java.util.List;

/**
 * 解析元数据
 */
public interface IDataSourceMetaService {

    /**
     * 解析元数据
     *
     * @return
     */
    List<DatabaseResource> getMeta();
}
