package com.dentiz.dentizapi.Controller;

import com.dentiz.dentizapi.Application.Application;
import com.dentiz.dentizapi.Config.JwtUtil;
import com.dentiz.dentizapi.Entity.DTO.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Application.API_BASE_PATH+"/auth")
public class LoginController {

    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  JwtUtil jwUtil;


    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDTO loginDTO) throws Exception {
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        //Authentication authentication = this.authenticationManager.authenticate(login);
        try {
             Authentication authentication = this.authenticationManager.authenticate(login);
        } catch (Exception e) {
            throw new Exception("Invalid username/password");
        }
        String token;
        try {
             token = this.jwUtil.createToken(loginDTO.getUsername());
        }catch (Exception e){
            throw new Exception("Error creating token");
        }

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
    }
}
