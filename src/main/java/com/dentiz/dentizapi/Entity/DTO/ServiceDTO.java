package com.dentiz.dentizapi.Entity.DTO;

import com.dentiz.dentizapi.Entity.PriceService;
import com.dentiz.dentizapi.Entity.ServiceEntity;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ServiceDTO {

    @Nullable
    private Integer idService;

    @Nullable
    private String name;

    @Nullable
    private  Double price;

    public ServiceDTO serviceToDTO(ServiceEntity service) {
        this.idService = service.getId();
        this.name = service.getName();
        return this;
    }


    public ServiceDTO priceServiceToDTO(PriceService priceServices) {
        this.idService = priceServices.getService().getId();
        this.name = priceServices.getService().getName();
        this.price = priceServices.getPrice();
        return this;
    }

}
