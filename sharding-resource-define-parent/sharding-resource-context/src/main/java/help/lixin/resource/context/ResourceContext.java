package help.lixin.resource.context;

import java.util.List;
import java.util.Map;

public interface ResourceContext {

    /**
     * 区域(比如:北京)
     *
     * @param region
     */
    public void setRegion(String region);

    public String getRegion();

    /**
     * 机房(比如:机房1)
     *
     * @param zone
     */
    public void setZone(String zone);

    public String getZone();

    public void setTenantId(String tenantId);

    public String getTenantId();

    public void setMicroServiceName(String microServiceName);

    public String getMicroServiceName();

    public void setEnv(String env);

    public List<String> getEnv();

    public void setProperties(Map<String, String> properties);

    public Map<String, String> getProperties();
}
