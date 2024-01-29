package com.dentiz.dentizapi.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "dentist_service")
@Getter
@Setter
public class DentistService {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id")
    private Dentist dentist;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dentistService")
    private List<PriceService> priceServices;
}
