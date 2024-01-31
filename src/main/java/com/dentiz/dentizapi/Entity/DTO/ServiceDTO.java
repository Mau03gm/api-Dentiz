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
    private List<Integer> idsServices;

    @Nullable
    private String name;

    @Nullable
    private List<String> names;

    @Nullable
    private List<Double> prices;

    @Nullable
    private  Double price;

    public ServiceDTO serviceToDTO(ServiceEntity service) {
        this.idService = service.getId();
        this.name = service.getName();
        return this;
    }

    public ServiceDTO listServiceToDTO(List<ServiceEntity> services) {
        this.idsServices = services.stream().map(ServiceEntity::getId).toList();
        this.names = services.stream().map(ServiceEntity::getName).toList();
        return this;
    }

    public ServiceDTO listPriceServiceToDTO(List<PriceService> priceServices) {
        this.idsServices = priceServices.stream().map(priceService -> priceService.getService().getId()).toList();
        this.names = priceServices.stream().map(priceService -> priceService.getService().getName()).toList();
        this.prices = priceServices.stream().map(PriceService::getPrice).toList();
        return this;
    }

    public ServiceDTO priceServiceToDTO(PriceService priceServices) {
        this.idService = priceServices.getService().getId();
        this.name = priceServices.getService().getName();
        this.price = priceServices.getPrice();
        return this;
    }

}
