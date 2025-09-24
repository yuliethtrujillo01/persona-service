package co.empresa.personaservice.domain.exception;

public class PersonaNoEncontradoException extends RuntimeException {
    public PersonaNoEncontradoException(Long id) {
        super("La persona con ID " + id + " no fue encontrado.");
    }
}