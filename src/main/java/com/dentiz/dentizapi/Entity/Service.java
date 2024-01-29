package com.dentiz.dentizapi.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "service")
@Getter
@Setter
public class Service {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 55)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "service")
    private List<PriceService> priceServices;

}
