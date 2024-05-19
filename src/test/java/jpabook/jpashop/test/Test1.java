package jpabook.jpashop.test;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.test.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(false)
public class Test1 {
    @Autowired
    EntityManager em;

    @Test
    public void test1() throws Exception {
        A a = new A();
        em.persist(a);

        B b = new B();
        em.persist(b);

        C c = new C();
        em.persist(c);

        a.setB(b);
        b.setC(c);
        
        em.flush();
        em.clear();

        A findA = em.createQuery("select a from A a", A.class)
                .getResultList()
                .get(0);

        findA.getB().getName(); // left join 으로 C 정보까지 같이 갖고 온다.
    }

    @Test
    public void test2() throws Exception {
        A a = new A();
        em.persist(a);
        
        em.flush();
        em.clear();

        System.out.println("-----------------------------");

        A findA = em.createQuery("select a from A a", A.class)
                .getResultList()
                .get(0);

        System.out.println("findA.getB() = " + findA.getB()); // null
    }
    
    @Test
    public void test3() throws Exception {
        A1 a1 = new A1();
        em.persist(a1);
        
        B1 b1 = new B1();
        em.persist(b1);
        
        a1.setB1(b1);
        
        em.flush();
        em.clear();

        A1 findA1 = em.find(A1.class, a1.getId());
        System.out.println("------------------------------");
        B1 findB1 = findA1.getB1();
        findB1.getName();
        System.out.println("findA1 == findB1.getA1() = " + (findA1 == findB1.getA1())); // true
    }

    @Test
    public void test4() throws Exception {
        A a = new A();
        em.persist(a);

        B b = new B();
        em.persist(b);

        C c = new C();
        em.persist(c);

        D d = new D();
        em.persist(d);

        a.setB(b);
        b.setC(c);
        c.setD(d);

        em.flush();
        em.clear();

        A findA = em.createQuery("select a from A a", A.class)
                .getResultList()
                .get(0);

        findA.getB().getName(); // b로 시작하여 left join c, left join d까지 해버린다.

        /*
        select
        b1_0.id,
        c1_0.id,
        d1_0.id,
        d1_0.name,
        c1_0.name,
        b1_0.name
            from
        b b1_0
            left join
        c c1_0
            on c1_0.id=b1_0.c_id
            left join
        d d1_0
            on d1_0.id=c1_0.d_id
        where
            b1_0.id=?
        **/
    }
}
