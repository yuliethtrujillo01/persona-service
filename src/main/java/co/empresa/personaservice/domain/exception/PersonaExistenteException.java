package co.empresa.personaservice.domain.exception;

public class PersonaExistenteException extends RuntimeException {
    public PersonaExistenteException(String nombre) {
        super("La persona con nombre '" + nombre + "' ya existe.");
    }
}
