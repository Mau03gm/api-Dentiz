package com.dentiz.dentizapi.Controller;

import com.dentiz.dentizapi.Config.Application.Application;
import com.dentiz.dentizapi.Entity.DTO.ServiceDTO;
import com.dentiz.dentizapi.Service.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Application.API_BASE_PATH+"/service")
public class ServiceController {

    @Autowired
    private ServicesService servicesService;

    @GetMapping("/")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        return ResponseEntity.ok().body(servicesService.getAllServices());
    }

    @PostMapping("/")
    public ResponseEntity<List<ServiceDTO>> addService(@RequestBody ServiceDTO service) {
        return ResponseEntity.ok().body(servicesService.addService(service));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<ServiceDTO>> deleteService(@PathVariable Integer id) {
        return ResponseEntity.ok().body(servicesService.deleteService(id));
    }

    @PostMapping("/ServicesDentist/{username}")
    public ResponseEntity addServicesToDentist(@PathVariable String username, @RequestBody List<ServiceDTO> services) throws Exception {
        servicesService.addServicesToDentist(services, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ServicesDentist/{username}")
    public ResponseEntity<List<ServiceDTO>> getServicesFromDentist(@PathVariable String username) throws Exception {
        return ResponseEntity.ok().body(servicesService.getAllServicesFromDentist(username));
    }

    @PatchMapping("/ServiceDentist/{username}")
    public ResponseEntity updateServiceToDentist(@PathVariable String username, @RequestBody ServiceDTO service) throws Exception {
        servicesService.updateServiceToDentist(service, username, service.getPrice());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/ServiceDentist/{username}")
    public ResponseEntity deleteServiceToDentist(@PathVariable String username, @RequestBody ServiceDTO service) throws Exception {
        servicesService.deleteServiceToDentist(service.getIdService(), username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ServiceDentist/{username}")
    public ResponseEntity<ServiceDTO> getServicePriceFromDentist(@PathVariable String username, @RequestBody ServiceDTO service ) throws Exception {
        return ResponseEntity.ok().body(servicesService.getPriceServiceFromDentist(username, service.getIdService()));
    }
}
