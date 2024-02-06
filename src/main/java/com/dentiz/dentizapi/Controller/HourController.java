package com.dentiz.dentizapi.Controller;

import com.dentiz.dentizapi.Config.Application.Application;
import com.dentiz.dentizapi.Entity.DTO.HoursDTO;
import com.dentiz.dentizapi.Service.HourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Application.API_BASE_PATH+"/hour")
public class HourController {

    @Autowired
    private HourService hourService;

    @PostMapping("/{username}")
    public ResponseEntity<HoursDTO> addHourToDentist(@PathVariable String username, @RequestBody String[] hours) throws Exception {
       HoursDTO hoursDTO= hourService.addHourToDentist(username, hours);
         return ResponseEntity.ok().body(hoursDTO);
    }

    @GetMapping("/{username}")
    public ResponseEntity<HoursDTO> getHours(@PathVariable String username) throws Exception {
       HoursDTO hoursDTO= hourService.getHoursByDentist(username);
         return ResponseEntity.ok().body(hoursDTO);
    }

    @PutMapping("/{username}")
    public ResponseEntity<HoursDTO> editHours(@PathVariable String username, @RequestBody String[] hours) throws Exception {
       HoursDTO hoursDTO= hourService.editHours(username, hours);
         return ResponseEntity.ok().body(hoursDTO);
    }
}
