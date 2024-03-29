package com.dentiz.dentizapi.Controller;

import com.dentiz.dentizapi.Config.Application.Application;
import com.dentiz.dentizapi.Entity.DTO.DentistProfileDTO;
import com.dentiz.dentizapi.Entity.DTO.StripeAccount;
import com.dentiz.dentizapi.Entity.Dentist;
import com.dentiz.dentizapi.Service.DentistDetailsService;
import com.dentiz.dentizapi.Service.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Application.API_BASE_PATH+"/dentist")
public class DentistController {

    @Autowired
    private DentistService dentistService;
    @Autowired
    private DentistDetailsService dentistDetailsService;


    @PatchMapping ("/profile/{username}")
    public ResponseEntity<DentistProfileDTO> editProfile(@RequestBody DentistProfileDTO dentistDTO, @PathVariable String username) throws Exception{
        return ResponseEntity.ok().body(dentistService.editProfile(dentistDTO, username));
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<DentistProfileDTO> getProfile(@PathVariable String username) throws Exception{
        return ResponseEntity.ok().body( dentistService.getProfile(username));
    }

    @PostMapping("/profile/image/{username}")
    public ResponseEntity<Void> uploadImage(@RequestParam("imageFile")MultipartFile imageFile, @PathVariable String username) throws Exception{
        dentistService.addImageToDentist( username, imageFile);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addPaymentMethod/{username}")
    public ResponseEntity<Void> addPaymentMethod(@RequestBody String paymentMethod, @PathVariable String username) throws Exception{
        Dentist dentist = dentistService.validateIfDentistExists(username, username);
        //dentistDetailsService.addDentistToDentistDetails(dentist, paymentMethod);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stripeAccount/{username}")
    public ResponseEntity<StripeAccount> setStripeAccount(@PathVariable String username) throws Exception{
        return ResponseEntity.ok().body(dentistService.StripeConnectAccount(username));
    }

    @GetMapping("/stripeAccount/{username}/dashboard")
    public ResponseEntity<String> getStripeDashboard(@PathVariable String username) throws Exception{
        return ResponseEntity.ok().body(dentistService.getDashboardLink(username));
    }

    @GetMapping("/stripeAccount/{username}/status")
    public ResponseEntity<String> getStripeAccountStatus(@PathVariable String username) throws Exception{
        return ResponseEntity.ok().body(dentistService.getConnectAccountStatus(username));
    }

}
