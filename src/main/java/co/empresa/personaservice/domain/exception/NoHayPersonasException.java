package co.empresa.personaservice.domain.exception;

public class NoHayPersonasException extends RuntimeException {
    public NoHayPersonasException() {
        super("No hay personas en la base de datos.");
    }
}