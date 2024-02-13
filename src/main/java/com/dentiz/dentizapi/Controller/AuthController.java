package com.dentiz.dentizapi.Controller;

import com.dentiz.dentizapi.Config.Application.Application;
import com.dentiz.dentizapi.Config.JwtUtil;
import com.dentiz.dentizapi.Entity.DTO.LoginDTO;
import com.dentiz.dentizapi.Entity.DTO.RegisterDentistDTO;
import com.dentiz.dentizapi.Service.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Application.API_BASE_PATH+"/auth")
public class AuthController {

    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private DentistService dentistService;
    @Autowired
    private  JwtUtil jwUtil;


    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDTO loginDTO) throws Exception {
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        try {
             Authentication authentication = this.authenticationManager.authenticate(login);
        } catch (Exception e) {
            throw new Exception("Username/Contraseña incorrectos");
        }
        String token;
        try {
             token = this.jwUtil.createToken(loginDTO.getUsername());
        }catch (Exception e){
            throw new Exception("Error creating token");
        }

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDentistDTO> registerDentist(@RequestBody RegisterDentistDTO registerDentistDTO) throws Exception{
        dentistService.registerDentist(registerDentistDTO);
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(registerDentistDTO.getUsername(), registerDentistDTO.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(login);
        String token = this.jwUtil.createToken(registerDentistDTO.getUsername());
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body(registerDentistDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "").build();
    }

    @GetMapping("/delete/{username}")
    public ResponseEntity<Void> deleteDentist(@PathVariable String username) throws Exception {
        dentistService.deleteDentist(username);
        return ResponseEntity.ok().build();
    }
}
