package help.lixin.sharding.resource.controller;

import help.lixin.sharding.resource.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    /**
     * 演示query
     *
     * @return
     */
    @GetMapping("/list")
    public List<Map> list() {
        List<Long> ids = new ArrayList<>();
        ids.add(565585450073325568L);
        ids.add(565585450987683840L);
        List<Map> maps = orderService.selectOrderbyIds(ids);
        return maps;
    }

    /**
     * 演示master插入数据.
     *
     * @param orderId
     * @param userId
     * @param price
     * @param status
     * @return
     */
    @PostMapping("/save/{orderId}/{userId}/{price}/{status}")
    public String save(@PathVariable("orderId") Long orderId, @PathVariable("userId") Long userId, @PathVariable("price") BigDecimal price, @PathVariable("status") String status) {
        boolean save = orderService.save(orderId, userId, price, status);
        return "SUCCESS";
    }

}
