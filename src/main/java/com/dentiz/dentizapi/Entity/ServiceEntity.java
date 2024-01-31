package com.dentiz.dentizapi.Entity;

import com.dentiz.dentizapi.Entity.DTO.ServiceDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "service")
@Getter
@Setter
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 55)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "service")
    private List<PriceService> priceServices;

    public ServiceEntity(ServiceDTO serviceDTO) {
        this.name = serviceDTO.getName();
    }

}
