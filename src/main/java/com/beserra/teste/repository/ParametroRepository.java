package com.beserra.teste.repository;

import com.beserra.teste.domain.Parametro;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Parametro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Long> {

}
