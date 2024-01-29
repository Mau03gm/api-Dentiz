package com.dentiz.dentizapi.Controller;

import com.dentiz.dentizapi.Application.Application;
import com.dentiz.dentizapi.Entity.DTO.EditDentistProfileDTO;
import com.dentiz.dentizapi.Service.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Application.API_BASE_PATH+"/dentist")
public class DentistController {

    @Autowired
    private DentistService dentistService;


    @PostMapping("/edit-profile/{username}")
    public ResponseEntity<EditDentistProfileDTO> editProfile(@RequestBody EditDentistProfileDTO registerDentistDTO, @PathVariable String username) throws Exception{
        dentistService.editProfile(registerDentistDTO, username);
        return ResponseEntity.ok().body(registerDentistDTO);
    }



}
