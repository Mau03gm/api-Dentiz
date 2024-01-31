package com.dentiz.dentizapi.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "dentist_details")
@Getter
@Setter
@NoArgsConstructor
public class DentistDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id")
    private Dentist dentist;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dentistDetails")
    private List<PriceService> priceServices;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hour_id")
    private Hour hour;

}
