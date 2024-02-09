package com.dentiz.dentizapi.Components.Stripe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Interval;

import java.math.BigDecimal;

@Entity
@Table(name = "plan")
@Getter
@Setter
@NoArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "stripe_id")
    private String stripeId;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private BigDecimal price; // Monto en centavos
    @Column(name = "currency")
    private String currency; // Moneda (ej. "eur")
    @Column(name = "free_trial_days")
    private Integer freeTrialDays;
}
