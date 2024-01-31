package com.dentiz.dentizapi.Controller;

import com.dentiz.dentizapi.Application.Application;
import com.dentiz.dentizapi.Entity.DTO.ServiceDTO;
import com.dentiz.dentizapi.Service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Application.API_BASE_PATH+"/service")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping("/getAll")
    public ResponseEntity<ServiceDTO> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    @GetMapping("/add")
    public ResponseEntity<ServiceDTO> addService(@RequestBody ServiceDTO service) {
        return ResponseEntity.ok(serviceService.addService(service));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ServiceDTO> deleteService(@PathVariable Integer id) {
        return ResponseEntity.ok(serviceService.deleteService(id));
    }

    @PostMapping("/addServiceToDentist/{username}")
    public ResponseEntity addServiceToDentist(@PathVariable String username, @RequestBody ServiceDTO service) throws Exception {
        serviceService.addServiceToDentist(service, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getServiceFromDentist/{username}")
    public ResponseEntity<ServiceDTO> getServiceFromDentist(@PathVariable String username) throws Exception {
        return ResponseEntity.ok(serviceService.getAllServiceFromDentist(username));
    }

    @PatchMapping("/updateServiceToDentist/{username}")
    public ResponseEntity updateServiceToDentist(@PathVariable String username, @RequestBody ServiceDTO service) throws Exception {
        serviceService.updateServiceToDentist(service.getIdService(), username, service.getPrice());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteServiceToDentist/{username}")
    public ResponseEntity deleteServiceToDentist(@PathVariable String username, @RequestBody ServiceDTO service) throws Exception {
        serviceService.deleteServiceToDentist(service.getIdService(), username);
        return ResponseEntity.ok().build();
    }
}
