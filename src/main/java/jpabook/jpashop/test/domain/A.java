package jpabook.jpashop.test.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class A {

    @Id @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_id")
    private B b;
}
