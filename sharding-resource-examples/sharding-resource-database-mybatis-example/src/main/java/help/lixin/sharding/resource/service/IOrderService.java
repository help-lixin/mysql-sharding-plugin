package help.lixin.sharding.resource.service;


import help.lixin.sharding.resource.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IOrderService {
    List<Map> selectOrderbyIds(List<Long> orderIds);

    boolean save(Long orderId, Long userId, BigDecimal price, String status);

    boolean save(Order order);
}
