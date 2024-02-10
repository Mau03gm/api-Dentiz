package com.dentiz.dentizapi.Entity;


import com.dentiz.dentizapi.Components.Stripe.Plan;
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

    @Column (name = "costumer_id", nullable = false)
    private String costumerId;

    @Column(name = "subscription_id")
    private String subscriptionId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id")
    private Dentist dentist;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dentistDetails")
    private List<PriceService> priceServices;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hour_id")
    private Hour hour;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dentistDetails")
    private List<Appointment> appointments;

}
