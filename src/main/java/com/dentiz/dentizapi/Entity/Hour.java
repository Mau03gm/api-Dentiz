package com.dentiz.dentizapi.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hour")
@Getter
@Setter
@NoArgsConstructor
public class Hour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hours")
    private String[] hours;

    public Hour(String[] hour) {
        this.hours = hour;
    }
}
