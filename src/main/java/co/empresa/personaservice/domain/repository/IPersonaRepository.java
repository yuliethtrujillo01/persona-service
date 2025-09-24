package co.empresa.personaservice.domain.repository;

import co.empresa.personaservice.domain.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface que hereda de JpaRepository para realizar las
 * operaciones de CRUD paginacion y ordenamiento sobre la entidad Persona
 */
public interface IPersonaRepository extends JpaRepository<Persona, Long> {
}
