package help.lixin.resource.context;

import java.util.List;
import java.util.Map;

public interface ResourceContextInfo {
    public void setTenantId(String tenantId);

    public String getTenantId();

    public void setMicroServiceName(String microServiceName);

    public String getMicroServiceName();

    public void setEnv(String env);

    public List<String> getEnv();

    public void setProperties(Map<String, String> properties);

    public Map<String, String> getProperties();
}
