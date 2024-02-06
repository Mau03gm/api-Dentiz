package com.dentiz.dentizapi.Repository;

import com.dentiz.dentizapi.Entity.Hour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HourRepository  extends JpaRepository<Hour, Long> {
}
