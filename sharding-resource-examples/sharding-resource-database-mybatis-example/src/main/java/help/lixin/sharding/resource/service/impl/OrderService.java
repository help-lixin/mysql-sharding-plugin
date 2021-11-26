package help.lixin.sharding.resource.service.impl;

import help.lixin.sharding.resource.mapper.OrderMapper;
import help.lixin.sharding.resource.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class OrderService implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * @Transactional 标记为只读, 所以, 会在多个slave数据源中获取一个数据源.如果, 没有配置slave, 则选择master.
     */
    @Override
    public List<Map> selectOrderbyIds(List<Long> orderIds) {
        List<Map> maps = orderMapper.selectOrderbyIds(orderIds);
        return maps;
    }

    /**
     * 演示事务以及往master写数据.
     *
     * @param orderId
     * @param userId
     * @param price
     * @param status
     * @return
     * @Transactional(readOnly = false , propagation = Propagation.REQUIRED)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean save(Long orderId, Long userId, BigDecimal price, String status) {
        int count = orderMapper.insertOrder(orderId, userId, price, status);
        if (count > 0) {
            return true;
        }
        return false;
    }
}
