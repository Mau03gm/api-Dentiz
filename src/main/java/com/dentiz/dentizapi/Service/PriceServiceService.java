package com.dentiz.dentizapi.Service;

import com.dentiz.dentizapi.Entity.DTO.ServiceDTO;
import com.dentiz.dentizapi.Entity.DentistDetails;
import com.dentiz.dentizapi.Entity.PriceService;
import com.dentiz.dentizapi.Entity.ServiceEntity;
import com.dentiz.dentizapi.Repository.PriceServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceServiceService {

    @Autowired
    private PriceServiceRepository priceServiceRepository;

    public void addPriceServiceToDentist(ServiceEntity service, DentistDetails dentistDetails, Double price) throws Exception {
        PriceService priceService = new PriceService(price, service, dentistDetails);
        try{
            priceServiceRepository.save(priceService);
        } catch (Exception e) {
            throw new Exception("PriceService already exists");
        }
    }

    public void updatePriceServiceToDentist(ServiceEntity service, DentistDetails dentistDetails, Double price) throws Exception {
        PriceService priceService = priceServiceRepository.findByServiceAndDentistDetails(service, dentistDetails);
        if(priceService == null) {
            throw new Exception("PriceService not found");
        }
        priceService.setPrice(price);
        priceServiceRepository.save(priceService);
    }

    public void deletePriceServiceToDentist(ServiceEntity service, DentistDetails dentistDetails) throws Exception {
        PriceService priceService = priceServiceRepository.findByServiceAndDentistDetails(service, dentistDetails);
        if(priceService == null) {
            throw new Exception("PriceService not found");
        }
        priceServiceRepository.delete(priceService);
    }

    public ServiceDTO getAllPriceServiceFromDentist(DentistDetails dentistDetails) throws Exception {
        List<PriceService> priceServices = priceServiceRepository.findByDentistDetails(dentistDetails);
        if(priceServices == null) {
            throw new Exception("PriceService not found");
        }
        ServiceDTO serviceDTO = new ServiceDTO();
        return serviceDTO.listPriceServiceToDTO(priceServices);
    }

    public ServiceDTO getServiceNamesFromDentist(DentistDetails dentistDetails) throws Exception {
        List<PriceService> priceServices = priceServiceRepository.findByDentistDetails(dentistDetails);
        if(priceServices == null) {
            throw new Exception("PriceService not found");
        }

        return new ServiceDTO().listPriceServiceToDTO(priceServices);
    }


    public ServiceDTO getPriceServiceFromDentist( ServiceEntity service, DentistDetails dentistDetails) throws Exception {
        PriceService priceService = priceServiceRepository.findByServiceAndDentistDetails(service, dentistDetails);
        if(priceService == null) {
            throw new Exception("PriceService not found");
        }
        return new ServiceDTO().priceServiceToDTO(priceService);
    }
}
