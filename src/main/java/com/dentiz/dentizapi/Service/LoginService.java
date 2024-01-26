package com.dentiz.dentizapi.Service;

import com.dentiz.dentizapi.Entity.Dentist;
import com.dentiz.dentizapi.Repository.DentistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {
    @Autowired
    private DentistRepository dentistRepository;;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Dentist dentist = dentistRepository.findByUsernameOrEmail(username, username);
        if (dentist == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
            return User.builder()
                    .username(dentist.getUsername())
                    .password(dentist.getPassword())
                    .roles("DENTIST")
                    .build();

    }


}
