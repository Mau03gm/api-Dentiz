package com.dentiz.dentizapi.Entity.DTO;

import lombok.Data;

@Data
public class RegisterDentistDTO {

    private String username;
    private String firstName;
    private String lastName;
    private String license;
    private String email;
    private String password;
    private String description;
}
