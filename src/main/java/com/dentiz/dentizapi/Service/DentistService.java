package com.dentiz.dentizapi.Service;

import com.dentiz.dentizapi.Entity.DTO.EditDentistProfileDTO;
import com.dentiz.dentizapi.Entity.DTO.RegisterDentistDTO;
import com.dentiz.dentizapi.Entity.Dentist;
import com.dentiz.dentizapi.Repository.DentistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DentistService {

    @Autowired
    private DentistRepository dentistRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public RegisterDentistDTO registerDentist(RegisterDentistDTO dentistDTO) throws Exception {
        validateIfDentistAlreadyExists(dentistDTO.getUsername(), dentistDTO.getEmail());
        Dentist dentist= new Dentist(dentistDTO);
        dentist.setPassword(passwordEncoder.encode(dentistDTO.getPassword()));
        dentistRepository.save(dentist);
        return dentistDTO;
    }

    public void editProfile(EditDentistProfileDTO dentistDTO, String username) throws Exception {
        Dentist dentist = validateIfDentistExists(username, username);
        dentist.updateDentistProfile(dentistDTO);
        dentistRepository.save(dentist);
    }

    private Dentist validateIfDentistExists(String username, String email) throws Exception {
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
}
