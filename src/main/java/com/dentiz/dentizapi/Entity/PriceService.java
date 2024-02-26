package com.dentiz.dentizapi.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "price_service")
@Getter
@Setter
@NoArgsConstructor
public class PriceService {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ServiceEntity service;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_details_id")
    private DentistDetails dentistDetails;

    public PriceService(Double price, ServiceEntity service, DentistDetails dentistDetails) {
        this.price = price;
        this.service = service;
        this.dentistDetails = dentistDetails;
    }
}
