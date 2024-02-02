package com.dentiz.dentizapi.Controller;

import com.dentiz.dentizapi.Application.Application;
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

    @GetMapping("/getAll")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        return ResponseEntity.ok(servicesService.getAllServices());
    }

    @GetMapping("/add")
    public ResponseEntity<List<ServiceDTO>> addService(@RequestBody ServiceDTO service) {
        return ResponseEntity.ok(servicesService.addService(service));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<List<ServiceDTO>> deleteService(@PathVariable Integer id) {
        return ResponseEntity.ok(servicesService.deleteService(id));
    }

    @PostMapping("/addAllServiceToDentist/{username}")
    public ResponseEntity addServicesToDentist(@PathVariable String username, @RequestBody List<ServiceDTO> services) throws Exception {
        servicesService.addServicesToDentist(services, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getServiceFromDentist/{username}")
    public ResponseEntity<List<ServiceDTO>> getServiceFromDentist(@PathVariable String username) throws Exception {
        return ResponseEntity.ok(servicesService.getAllServiceFromDentist(username));
    }

    @PatchMapping("/updateServiceToDentist/{username}")
    public ResponseEntity updateServiceToDentist(@PathVariable String username, @RequestBody ServiceDTO service) throws Exception {
        servicesService.updateServiceToDentist(service, username, service.getPrice());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteServiceToDentist/{username}")
    public ResponseEntity deleteServiceToDentist(@PathVariable String username, @RequestBody Integer service) throws Exception {
        servicesService.deleteServiceToDentist(service, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getServicePriceFromDentist/{username}")
    public ResponseEntity<ServiceDTO> getServicePriceFromDentist(@PathVariable String username, @RequestBody Integer service ) throws Exception {
        return ResponseEntity.ok(servicesService.getPriceServiceFromDentist(username, service));
    }
}
