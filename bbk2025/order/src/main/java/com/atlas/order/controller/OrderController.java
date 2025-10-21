package com.atlas.order.controller;

import com.atlas.order.dto.CreateOrderReq;
import com.atlas.order.dto.OrderDto;
import com.atlas.order.dto.PaymentReq;
import com.atlas.order.model.Order;
import com.atlas.order.repo.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping
public class OrderController {
    private final OrderRepository repo;
    private final AmqpTemplate amqp;

    public OrderController(OrderRepository repo, AmqpTemplate amqp){ this.repo=repo; this.amqp=amqp; }

    @PostMapping("/orders")
    public OrderDto create(@RequestBody CreateOrderReq req){
        Order o = new Order(); o.setUserEmail(req.userEmail()); o.setTotal(req.total());
        o = repo.save(o);
        amqp.convertAndSend("payments.exchange", "pay",
                new PaymentReq(o.getId(), o.getTotal(), o.getUserEmail()));
        return new OrderDto(o.getId(), o.getUserEmail(), o.getTotal(), o.getStatus().name());
    }

    @GetMapping("/orders/{id}")
    public OrderDto get(@PathVariable Long id){
        var o = repo.findById(id).orElseThrow();
        return new OrderDto(o.getId(), o.getUserEmail(), o.getTotal(), o.getStatus().name());
    }

    // basit login rate-limit
    private final Map<String, int[]> buckets = new ConcurrentHashMap<>();
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(HttpServletRequest req){
        var ip = req.getRemoteAddr();
        var nowSec = (int)(System.currentTimeMillis()/1000);
        var arr = buckets.computeIfAbsent(ip, k -> new int[]{nowSec,0});
        if (arr[0] != nowSec) { arr[0]=nowSec; arr[1]=0; }
        if (++arr[1] > 10) return ResponseEntity.status(429).body(Map.of("error","Too Many Requests"));
        return ResponseEntity.ok(Map.of("token","demo-token"));
    }

    // worker geri çağırır
    @PostMapping("/internal/orders/{id}/pay")
    public void mark(@PathVariable Long id, @RequestParam String status){
        var o = repo.findById(id).orElseThrow();
        o.setStatus(Order.Status.valueOf(status)); // PAID/FAILED
        repo.save(o);
    }
}
