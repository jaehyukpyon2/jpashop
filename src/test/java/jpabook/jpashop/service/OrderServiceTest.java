package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        Member member = createMember();

        Item book = createBook("시골 JPA", 10_000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");

        em.flush();
        em.clear();

        System.out.println("-------------------------EM CLEAR-------------------------");

        Order findOrder = orderRepository.findOne(orderId); // JOIN query 없이 오직 order만 조회
        System.out.println("findOrder.getDelivery().getClass() = " + findOrder.getDelivery().getClass()); // Proxy
        Delivery findDelivery = findOrder.getDelivery(); // select query 안 나감
    }

    @Test
    public void 상품주문_재고수량_초과() throws Exception {
        Member member = createMember();
        Item book = createBook("시골 JPA", 10_000, 10);

        int orderCount = 11;

        assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), book.getId(), orderCount));
    }

    @Test
    public void 주문취소() throws Exception {
        Member member = createMember();
        Item item = createBook("시골 JPA", 10_000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        orderService.cancelOrder(orderId);
        // order를 cancel 하면, item에는 수량에 대한 update query가 나가지 않는다. 왜? 어차피 원복시켜버렸기 때문에
        // 하지만 order는 update query가 나간다. 왜? status를 cancel로 변경시켜야 하기 때문에

        Order getOrder = orderRepository.findOne(orderId);
    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    @Test
    public void proxy_테스트() throws Exception {
        Member member = new Member();
        member.setName("member 1");
        em.persist(member);

        em.flush();
        em.clear();

        Member referenceMember = em.getReference(Member.class, member.getId()); // select query X
        System.out.println("---------------------------------------");
        Member findMember = em.find(Member.class, member.getId()); // 여기에서 member select query 실행
        System.out.println("referenceMember == findMember ? " + (referenceMember == findMember)); // true
        System.out.println("findMember.getClass() ? " + findMember.getClass()); // proxy!
    }

    @Test
    public void proxy_테스트2() throws Exception {
        Member member = new Member();
        member.setName("member 1");
        em.persist(member);

        em.flush();
        em.clear();

        Member referenceMember = em.getReference(Member.class, member.getId()); // select query X
        System.out.println("---------------------------------------");

        Member findMember = em.createQuery("select m from Member m", Member.class).getResultList().get(0);

        System.out.println("referenceMember == findMember ? " + (referenceMember == findMember)); // true
        System.out.println("findMember.getClass() ? " + findMember.getClass()); // proxy!
        System.out.println(referenceMember.getName() + ", " + referenceMember.getId()); // "member 1", 1
    }

    @Test
    public void proxy_테스트3() throws Exception {
        Member member = new Member();
        member.setId(1L);
        member.setName("member1");
        em.persist(member);

        em.flush();
        em.clear();

        Member newPersistMember = new Member();
        newPersistMember.setId(1L);
        member.setName("newPersistMember");
        em.persist(newPersistMember);

        Member findMember = em.createQuery("select m from Member m where m.id = 1", Member.class)
                .getResultList().get(0);
        // primary key violation exception => jpql이 실행되기 전에 영속성 캐시를 flush하는데, 1L이 중복이라서...

        System.out.println("member == newPersistMember ? " + (newPersistMember == findMember));
    }
}