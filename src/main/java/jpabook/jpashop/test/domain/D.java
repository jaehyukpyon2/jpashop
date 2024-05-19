package jpabook.jpashop.test.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class D {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
