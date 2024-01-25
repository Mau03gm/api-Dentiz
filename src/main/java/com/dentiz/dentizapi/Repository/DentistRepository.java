package com.dentiz.dentizapi.Repository;


import com.dentiz.dentizapi.Entity.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DentistRepository extends JpaRepository<Dentist, String> {
    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email")
    Dentist findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
}
