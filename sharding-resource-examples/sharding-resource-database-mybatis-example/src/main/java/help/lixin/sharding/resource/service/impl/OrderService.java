package help.lixin.sharding.resource.service.impl;

import help.lixin.sharding.resource.mapper.OrderMapper;
import help.lixin.sharding.resource.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class OrderService implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<Map> selectOrderbyIds(List<Long> orderIds) {
        List<Map> maps = orderMapper.selectOrderbyIds(orderIds);
        return maps;
    }
}
