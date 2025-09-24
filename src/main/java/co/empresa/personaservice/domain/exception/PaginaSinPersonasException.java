package co.empresa.personaservice.domain.exception;

public class PaginaSinPersonasException extends RuntimeException {
    public PaginaSinPersonasException(int page) {
        super("No hay personas en la página solicitada: " + page);
    }
}