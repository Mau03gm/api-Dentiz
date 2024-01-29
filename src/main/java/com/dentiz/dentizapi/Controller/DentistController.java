package com.dentiz.dentizapi.Controller;

import com.dentiz.dentizapi.Application.Application;
import com.dentiz.dentizapi.Entity.DTO.RegisterDentistDTO;
import com.dentiz.dentizapi.Service.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Application.API_BASE_PATH+"/dentist")
public class DentistController {

    @Autowired
    private DentistService dentistService;

    @PostMapping("/register")
    public ResponseEntity<RegisterDentistDTO> registerDentist(@RequestBody RegisterDentistDTO registerDentistDTO) throws Exception{
       dentistService.registerDentist(registerDentistDTO);
         return ResponseEntity.ok(registerDentistDTO);
    }

    
}
