package com.dentiz.dentizapi.Service;

import com.dentiz.dentizapi.Components.Stripe.Plan;
import com.dentiz.dentizapi.Components.Images.DataSource.BucketObject;
import com.dentiz.dentizapi.Components.Images.DataSource.S3DataSource;
import com.dentiz.dentizapi.Components.Stripe.Service.Services.StripeServices;
import com.dentiz.dentizapi.Components.Stripe.Service.Subscription.StripeSubscriptions;
import com.dentiz.dentizapi.Entity.DTO.DentistProfileDTO;
import com.dentiz.dentizapi.Entity.DTO.RegisterDentistDTO;
import com.dentiz.dentizapi.Entity.DTO.StripeAccount;
import com.dentiz.dentizapi.Entity.Dentist;
import com.dentiz.dentizapi.Repository.DentistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DentistService {

    @Autowired
    private DentistRepository dentistRepository;
    @Autowired
    private DentistDetailsService dentistDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StripeSubscriptions stripeSubscriptions;

    @Autowired
    private StripeServices stripeServices;

    @Autowired
    private S3DataSource s3DataSource;

    public RegisterDentistDTO registerDentist(RegisterDentistDTO dentistDTO) throws Exception {
        validateIfDentistAlreadyExists(dentistDTO.getUsername(), dentistDTO.getEmail());
        Dentist dentist= new Dentist(dentistDTO);
        dentist.setPassword(passwordEncoder.encode(dentistDTO.getPassword()));
        String paymentMethod= dentistDTO.getPaymentMethod();
        String costumerId= stripeSubscriptions.createCostumer(dentist, paymentMethod);
        Plan plan = stripeSubscriptions.getPlan("Basic");
        String subscriptionId= stripeSubscriptions.createCostumerSubscription(costumerId,plan ,paymentMethod);
        dentistRepository.save(dentist);
        dentistDetailsService.addDentistToDentistDetails(dentist, costumerId, plan, subscriptionId);
        return dentistDTO;
    }

    public DentistProfileDTO editProfile(DentistProfileDTO dentistDTO, String username) throws Exception {
        Dentist dentist = validateIfDentistExists(username, username);
        dentist.updateDentistProfile(dentistDTO);
         return new DentistProfileDTO(dentistRepository.save(dentist));
    }

    public DentistProfileDTO getProfile(String username) throws Exception {
        Dentist dentist = validateIfDentistExists(username, username);
        DentistProfileDTO dentistProfileDTO = new DentistProfileDTO(dentist);
        return dentistProfileDTO;
    }

    public Dentist validateIfDentistExists(String username, String email) throws Exception {
        Dentist dentist = dentistRepository.findByUsernameOrEmail(username, email);
        if (dentist==null) {
            throw new UsernameNotFoundException("Dentist not found");
        }
        if(!stripeSubscriptions.validateDentistSubscription(dentist)){
            throw new Exception("Dentista no tiene suscripción");
        }
        return dentist;
    }

    public StripeAccount StripeConnectAccount(String username) throws Exception {
        Dentist dentist = validateIfDentistExists(username, username);
        if(dentist.getAccountStripeId()!=null){
            return new StripeAccount(stripeServices.createAccountLink(dentist));
        }
        String accountStripeId= stripeServices.createAccountStripeConnect(dentist);
        dentist.setAccountStripeId(accountStripeId);
        String accountStripeLink = stripeServices.createAccountLink(dentist);
        dentistRepository.save(dentist);
        return new StripeAccount(accountStripeLink);
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
        stripeSubscriptions.deleteCostumerSubscription(dentist.getDentistDetails().getSubscriptionId());
        stripeSubscriptions.deleteCostumer(dentist.getDentistDetails().getCostumerId());
        dentistRepository.delete(dentist);
    }

    public DentistProfileDTO addImageToDentist(String username, MultipartFile file) throws Exception {
        Dentist dentist = validateIfDentistExists(username, username);
         BucketObject bucketObject = s3DataSource.uploadFile(file);
        dentist.setURLImage(bucketObject.getFileName());
        return new DentistProfileDTO(dentistRepository.save(dentist));
    }
}
