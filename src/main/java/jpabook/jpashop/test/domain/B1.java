package jpabook.jpashop.test.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class B1 {
    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "b1")
    private A1 a1;
}
