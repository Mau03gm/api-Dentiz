package com.dentiz.dentizapi.Service;

import com.dentiz.dentizapi.Entity.DTO.ServiceDTO;
import com.dentiz.dentizapi.Entity.Dentist;
import com.dentiz.dentizapi.Repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dentiz.dentizapi.Entity.ServiceEntity;

import java.util.List;

@Service
public class ServicesService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private DentistService dentistService;

    @Autowired
    private PriceServiceService priceServiceService;

    public List<ServiceDTO> getAllServices() {
        List <ServiceEntity> services = serviceRepository.findAll();
        ServiceDTO serviceDTO = new ServiceDTO();
        return services.stream().map(serviceDTO::serviceToDTO).toList();
    }

    public List<ServiceDTO> addService(ServiceDTO service) {
        ServiceEntity serviceEntity = new ServiceEntity(service);
        return getAllServices();
    }

    public List<ServiceDTO> deleteService(Integer id) {
        serviceRepository.deleteById(id);
        return getAllServices();
    }

    public List<ServiceDTO> updateService(ServiceDTO service) {
        ServiceEntity serviceEntity = new ServiceEntity(service);
        serviceRepository.save(serviceEntity);
        return getAllServices();
    }

    public ServiceEntity getService(Integer id) throws Exception{
        ServiceEntity service= serviceRepository.findById(id).orElse(null);
        if(service == null) {
            throw new Exception("Servicio no encontrado");
        }
        return service;
    }

    public void addServicesToDentist(List<ServiceDTO> services, String username) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);

        int indexPriceService;
        for(indexPriceService=0; indexPriceService<services.size(); indexPriceService++) {
            ServiceEntity service = serviceRepository.findById(services.get(indexPriceService).getIdService()).orElse(null);
            if(service == null) {
                throw new Exception("Servicio no encontrado");
            }
            priceServiceService.addPriceServiceToDentist(service, dentist.getDentistDetails(), services.get(indexPriceService).getPrice());
        }
    }

    public void updateServiceToDentist(ServiceDTO serviceDTO, String username, Double price) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        ServiceEntity serviceEntity = serviceRepository.findById(serviceDTO.getIdService()).orElse(null);
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

    public List<ServiceDTO> getAllServiceFromDentist(String username) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        return  priceServiceService.getAllPriceServiceFromDentist(dentist.getDentistDetails());
    }

    public ServiceDTO getPriceServiceFromDentist(String username, Integer idService) throws Exception {
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        ServiceEntity service = serviceRepository.findById(idService).orElse(null);
        if(service == null) {
            throw new Exception("Servicio no encontrado");
        }
        return priceServiceService.getPriceServiceFromDentist(service, dentist.getDentistDetails());
    }

}
