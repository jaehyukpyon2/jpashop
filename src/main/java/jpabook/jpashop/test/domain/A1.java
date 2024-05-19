package jpabook.jpashop.test.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class A1 {
    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b1_id")
    private B1 b1;
}
