package com.dentiz.dentizapi.Repository;

import com.dentiz.dentizapi.Entity.DentistDetails;
import com.dentiz.dentizapi.Entity.PriceService;
import com.dentiz.dentizapi.Entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceServiceRepository extends JpaRepository<PriceService, Long> {

    PriceService findByServiceAndDentistDetails(ServiceEntity service, DentistDetails dentistDetails);

    List<PriceService> findByDentistDetails(DentistDetails dentistDetails);

    PriceService findByServiceId(Integer id);
}
