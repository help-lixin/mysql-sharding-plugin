package help.lixin.sharding.resource.controller;

import help.lixin.sharding.resource.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("/list")
    public List<Map> list() {
        List<Long> ids = new ArrayList<>();
        ids.add(565585450073325568L);
        ids.add(565585450987683840L);
        List<Map> maps = orderService.selectOrderbyIds(ids);
        return maps;
    }
}
