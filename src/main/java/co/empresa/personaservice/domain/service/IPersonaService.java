package co.empresa.personaservice.domain.service;


import co.empresa.personaservice.domain.model.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface que define los métodos que se pueden realizar sobre la entidad Persona
 */
public interface IPersonaService {
    Persona save(Persona persona);
    void delete(Persona persona);
    Optional<Persona> findById(Long id);
    Persona update(Persona persona);
    List<Persona> findAll();
    Page<Persona> findAll(Pageable pageable);
}
