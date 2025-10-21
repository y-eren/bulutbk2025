package com.payment.worker.service;

import com.payment.worker.dto.PaymentReq;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentConsumer {

    private final RestTemplate rt = new RestTemplate();

    @Value("${order.api.base-url:http://localhost:8080}")
    private String orderApiBaseUrl;

    @RabbitListener(
            queuesToDeclare = @Queue(value = "payments.queue", durable = "true"),
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void consume(PaymentReq req) {
        boolean fail = ThreadLocalRandom.current().nextInt(100) < 30; // %30 hata simülasyonu
        mark(req.getOrderId(), fail ? "FAILED" : "PAID");
        System.out.println("Consumed order " + req.getOrderId() + " => " + (fail ? "FAILED" : "PAID"));
    }

    private void mark(Long id, String status) {
        String url = orderApiBaseUrl + "/internal/orders/{id}/pay?status={s}";
        rt.postForObject(url, null, Void.class, Map.of("id", id, "s", status));
    }
}
