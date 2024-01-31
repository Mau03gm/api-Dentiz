package com.dentiz.dentizapi.Service;

import com.dentiz.dentizapi.Entity.DTO.ServiceDTO;
import com.dentiz.dentizapi.Entity.Dentist;
import com.dentiz.dentizapi.Repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dentiz.dentizapi.Entity.ServiceEntity;

import java.util.List;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private DentistService dentistService;

    @Autowired
    private PriceServiceService priceServiceService;

    public ServiceDTO getAllServices() {
        List <ServiceEntity> services = serviceRepository.findAll();
        return new ServiceDTO().listServiceToDTO(services);
    }

    public ServiceDTO addService(ServiceDTO service) {
        ServiceEntity serviceEntity = new ServiceEntity(service);
        return getAllServices();
    }

    public ServiceDTO deleteService(Integer id) {
        serviceRepository.deleteById(id);
        return getAllServices();
    }

    public ServiceDTO updateService(ServiceDTO service) {
        ServiceEntity serviceEntity = new ServiceEntity(service);
        serviceRepository.save(serviceEntity);
        return getAllServices();
    }

    public void addServiceToDentist(ServiceDTO service, String username) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);

        if(service.getIdsServices().size() != service.getPrices().size() ) {
            throw new Exception("Error en la cantidad de servicios y precios");
        }
        int indexPriceService ;
        for(indexPriceService=0; indexPriceService<service.getIdsServices().size(); indexPriceService++) {
            ServiceEntity serviceEntity = serviceRepository.findById(service.getIdsServices().get(indexPriceService)).orElse(null);
            if(serviceEntity == null) {
                throw new Exception("Servicio no encontrado");
            }
            priceServiceService.addPriceServiceToDentist(serviceEntity, dentist.getDentistDetails(), service.getPrices().get(indexPriceService));
        }
    }

    public void updateServiceToDentist(Integer serviceId, String username, Double price) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        ServiceEntity serviceEntity = serviceRepository.findById(serviceId).orElse(null);
        if(serviceEntity == null) {
            throw new Exception("Servicio no encontrado");
        }
        priceServiceService.updatePriceServiceToDentist(serviceEntity, dentist.getDentistDetails(), price);
    }

    public void deleteServiceToDentist(Integer serviceId, String username) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        ServiceEntity serviceEntity = serviceRepository.findById(serviceId).orElse(null);
        if(serviceEntity == null) {
            throw new Exception("Servicio no encontrado");
        }
        priceServiceService.deletePriceServiceToDentist(serviceEntity, dentist.getDentistDetails());
    }

    public ServiceDTO getAllServiceFromDentist(String username) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        return priceServiceService.getAllPriceServiceFromDentist(dentist.getDentistDetails());
    }

}
