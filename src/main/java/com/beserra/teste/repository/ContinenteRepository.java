package com.beserra.teste.repository;

import com.beserra.teste.domain.Continente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Continente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContinenteRepository extends JpaRepository<Continente, Long> {

}
