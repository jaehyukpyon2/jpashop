package jpabook.jpashop.test.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class B {

    @Id @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "c_id")
    private C c;
}
