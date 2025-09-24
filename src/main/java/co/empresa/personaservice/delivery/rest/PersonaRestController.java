package co.empresa.personaservice.delivery.rest;

import co.empresa.personaservice.domain.exception.NoHayPersonasException;
import co.empresa.personaservice.domain.exception.PaginaSinPersonasException;
import co.empresa.personaservice.domain.exception.PersonaNoEncontradoException;
import co.empresa.personaservice.domain.exception.ValidationException;
import co.empresa.personaservice.domain.model.Persona;
import co.empresa.personaservice.domain.service.IPersonaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/persona-service")
public class PersonaRestController {

    // Declaramos como final el servicio para mejorar la inmutabilidad
    private final IPersonaService personaService;

    // Constantes para los mensajes de respuesta
    private static final String MENSAJE = "mensaje";
    private static final String PERSONA = "persona";
    private static final String PERSONAS = "personas";

    // Inyección de dependencia del servicio que proporciona servicios de CRUD
    public PersonaRestController(IPersonaService personaService) {
        this.personaService = personaService;
    }

    /**
     * Listar todos las personas.
     */
    @GetMapping("/personas")
    public ResponseEntity<Map<String, Object>> getPersonas() {
        List<Persona> personas = personaService.findAll();
        if (personas.isEmpty()) {
            throw new NoHayPersonasException();
        }
        Map<String, Object> response = new HashMap<>();
        response.put(PERSONAS, personas);
        return ResponseEntity.ok(response);
    }

    /**
     * Listar personas con paginación.
     */
    @GetMapping("/persona/page/{page}")
    public ResponseEntity<Object> index(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        Page<Persona> personas = personaService.findAll(pageable);
        if (personas.isEmpty()) {
            throw new PaginaSinPersonasException(page);
        }
        return ResponseEntity.ok(personas);
    }

    /**
     * Crear un nueva persona pasando el objeto en el cuerpo de la petición, usando validaciones
     */
    @PostMapping("/personas")
    public ResponseEntity<Map<String, Object>> save(@Valid @RequestBody Persona persona, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        Map<String, Object> response = new HashMap<>();
        Persona nuevoPersona = personaService.save(persona);
        response.put(MENSAJE, "La persona ha sido creada con éxito!");
        response.put(PERSONA, nuevoPersona);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Eliminar una persona pasando el objeto en el cuerpo de la petición.
     */
    @DeleteMapping("/personas")
    public ResponseEntity<Map<String, Object>> delete(@RequestBody Persona persona) {
        personaService.findById(persona.getId())
            .orElseThrow(() -> new PersonaNoEncontradoException(persona.getId()));
        personaService.delete(persona);
        Map<String, Object> response = new HashMap<>();
        response.put(MENSAJE, "La persona ha sido eliminada con éxito!");
        response.put(PERSONA, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualizar una persona pasando el objeto en el cuerpo de la petición.
     * @param persona: Objeto Persona que se va a actualizar
     */
    @PutMapping("/personas")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody Persona persona, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        personaService.findById(persona.getId())
                .orElseThrow(() -> new PersonaNoEncontradoException(persona.getId()));
        Map<String, Object> response = new HashMap<>();
        Persona personaActualizado = personaService.update(persona);
        response.put(MENSAJE, "La persona ha sido actualizada con éxito!");
        response.put(PERSONA, personaActualizado);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener una persona por su ID.
     */
    @GetMapping("/personas/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Persona persona = personaService.findById(id)
                .orElseThrow(() -> new PersonaNoEncontradoException(id));
        Map<String, Object> response = new HashMap<>();
        response.put(MENSAJE, "La persona ha sido encontrada con éxito!");
        response.put(PERSONA, persona);
        return ResponseEntity.ok(response);
    }
}
