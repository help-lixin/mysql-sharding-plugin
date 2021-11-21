package help.lixin.datasource.meta.impl;

import help.lixin.datasource.meta.IDataSourceMetaService;
import help.lixin.datasource.model.DatabaseResource;
import help.lixin.resource.event.Event;
import help.lixin.resource.listener.IEventListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 通过组合模式,进行缓存
 */
public class CacheDataSourceMetaService implements IDataSourceMetaService, IEventListener {

    private IDataSourceMetaService dataSourceMetaService;

    private final CopyOnWriteArrayList<DatabaseResource> metas = new CopyOnWriteArrayList<DatabaseResource>();

    public CacheDataSourceMetaService(IDataSourceMetaService dataSourceMetaService) {
        this.dataSourceMetaService = dataSourceMetaService;
    }

    @Override
    public List<DatabaseResource> getMeta() {
        if (!metas.isEmpty()) {
            return metas;
        } else {
            List<DatabaseResource> tmpMetas = dataSourceMetaService.getMeta();
            metas.addAll(tmpMetas);
            return metas;
        }
    }

    /**
     * 遇到刷新事件时,触发清空缓存里的数据.
     *
     * @param event
     */
    @Override
    public void onEvent(Event event) {
        metas.clear();
        // 清空后,手动进行加载一次元数据.
        getMeta();
    }
}
