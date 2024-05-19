package jpabook.jpashop.test.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class C {

    @Id @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "d_id")
    private D d;
}
