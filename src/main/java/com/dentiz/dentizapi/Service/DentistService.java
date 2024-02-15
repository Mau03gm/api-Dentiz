package com.dentiz.dentizapi.Service;

import com.dentiz.dentizapi.Components.Stripe.Service.StripeService;
import com.dentiz.dentizapi.Entity.DTO.DentistProfileDTO;
import com.dentiz.dentizapi.Entity.DTO.RegisterDentistDTO;
import com.dentiz.dentizapi.Entity.Dentist;
import com.dentiz.dentizapi.Repository.DentistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DentistService {

    @Autowired
    private DentistRepository dentistRepository;
    @Autowired
    private DentistDetailsService dentistDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StripeService stripeService;

    public RegisterDentistDTO registerDentist(RegisterDentistDTO dentistDTO) throws Exception {
        validateIfDentistAlreadyExists(dentistDTO.getUsername(), dentistDTO.getEmail());
        Dentist dentist= new Dentist(dentistDTO);
        dentist.setPassword(passwordEncoder.encode(dentistDTO.getPassword()));
        dentistRepository.save(dentist);
        dentistDetailsService.addDentistToDentistDetails(dentist, dentistDTO.getToken());
        return dentistDTO;
    }

    public void editProfile(DentistProfileDTO dentistDTO, String username) throws Exception {
        Dentist dentist = validateIfDentistExists(username, username);
        dentist.updateDentistProfile(dentistDTO);
        dentistRepository.save(dentist);
    }

    public DentistProfileDTO getProfile(String username) throws Exception {
        Dentist dentist = validateIfDentistExists(username, username);
        DentistProfileDTO dentistProfileDTO = new DentistProfileDTO(dentist);
        return dentistProfileDTO;
    }

    public Dentist validateIfDentistExists(String username, String email) throws Exception {
        Dentist dentist = dentistRepository.findByUsernameOrEmail(username, email);
        if (dentist==null) {
            throw new Exception("Dentista no encontrado");
        }
        return dentist;
    }

    private void validateIfDentistAlreadyExists(String username, String email) throws Exception {
        Dentist dentist = dentistRepository.findByUsernameOrEmail(username, email);
        if (dentist!=null) {
            throw new Exception("Dentista ya existe");
        }
    }

    private void usernameAlreadyExists(String username) throws Exception {
        Dentist dentist = dentistRepository.findById(username).orElse(null);
        if (dentist!=null) {
            throw new Exception("Nombre de usuario en uso");
        }
    }

    public void deleteDentist(String username) throws Exception {
        Dentist dentist = validateIfDentistExists(username, username);
        //dentistDetailsService.deleteDentistDetails(dentist);
        stripeService.deleteCostumerSubscription(dentist.getDentistDetails().getSubscriptionId());
        stripeService.deleteCostumer(dentist.getDentistDetails().getCostumerId());
        dentistRepository.delete(dentist);
    }
}
