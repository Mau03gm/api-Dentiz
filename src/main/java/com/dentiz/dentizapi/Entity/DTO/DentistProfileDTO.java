package com.dentiz.dentizapi.Entity.DTO;

import com.dentiz.dentizapi.Entity.Dentist;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class DentistProfileDTO {

    @Nullable
    private String username;
    private String firstName;
    private String lastName;
    private String license;
    private String description;
    @Nullable
    private String accountStripeId;
    @Nullable
    private String photoProfileUrl;
    @Nullable
    private byte[] photoProfileImage;

    public DentistProfileDTO(Dentist dentist) {
        this.username = dentist.getUsername();
        this.firstName = dentist.getFirstName();
        this.lastName = dentist.getLastName();
        this.license = dentist.getLicense();
        this.description = dentist.getDescription();
    }

    public DentistProfileDTO() {
    }
}
