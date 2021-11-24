package help.lixin.sharding.resource.service;


import java.util.List;
import java.util.Map;

public interface IOrderService {
    List<Map> selectOrderbyIds(List<Long> orderIds);
}
