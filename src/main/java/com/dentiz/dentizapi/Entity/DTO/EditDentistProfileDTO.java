package com.dentiz.dentizapi.Entity.DTO;

import lombok.Data;

@Data
public class EditDentistProfileDTO {

    private String username;
    private String firstName;
    private String lastName;
    private String license;
    private String description;
}
