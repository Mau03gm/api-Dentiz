package com.dentiz.dentizapi.Controller;

import com.dentiz.dentizapi.Config.Application.Application;
import com.dentiz.dentizapi.Entity.DTO.DentistProfileDTO;
import com.dentiz.dentizapi.Service.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Application.API_BASE_PATH+"/dentist")
public class DentistController {

    @Autowired
    private DentistService dentistService;


    @PostMapping("/profile/{username}")
    public ResponseEntity<DentistProfileDTO> editProfile(@RequestBody DentistProfileDTO dentistDTO, @PathVariable String username) throws Exception{
        dentistService.editProfile(dentistDTO, username);
        return ResponseEntity.ok().body(dentistDTO);
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<DentistProfileDTO> getProfile(@PathVariable String username) throws Exception{
        DentistProfileDTO dentistProfileDTO = dentistService.getProfile(username);
        return ResponseEntity.ok().body(dentistProfileDTO);
    }

}
