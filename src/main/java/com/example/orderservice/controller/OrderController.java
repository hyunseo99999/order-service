package com.example.orderservice.controller;

import com.example.orderservice.domain.OrderEntity;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.math.raw.Mod;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "order-service healthCheck";
    }

    @PostMapping("{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,  @RequestBody RequestOrder orderDetails) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
        orderDto.setUserId(userId);

        OrderDto createOrder = orderService.createOrder(orderDto);

        ResponseOrder responseOrder = mapper.map(createOrder, ResponseOrder.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) {
        Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);
        List<ResponseOrder> result = new ArrayList<>();
        orderList.forEach((item) -> {
            result.add(new ModelMapper().map(item, ResponseOrder.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
