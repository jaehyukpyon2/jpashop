package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();

        System.out.println("---------------");
        System.out.println("order.getDelivery().getOrder().getClass() =>"
                + (order.getDelivery().getOrder().getClass())); // PROXY 아님
        // 즉, Delivery의 Order 필드를 채우기 위한
        // Order 테이블에 대한 delivery_id = ? 쿼리가 안 나간다.
        System.out.println("---------------");
    }
}
