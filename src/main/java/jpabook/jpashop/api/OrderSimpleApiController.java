package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        System.out.println("-----------start---------------");
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        System.out.println("------------end--------------");
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<OrderSimpleQueryDto> orderV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderSimpleQueryDto> result = orders.stream()
                .map(o -> new OrderSimpleQueryDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<OrderSimpleQueryDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(); // Query 한 번 실행
        List<OrderSimpleQueryDto> result = orders.stream()
                .map(o -> new OrderSimpleQueryDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4() {
        List<OrderSimpleQueryDto> result = orderSimpleQueryRepository.findOrderDtos();
        return result;
    }
}
