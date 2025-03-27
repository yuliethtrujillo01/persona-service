package co.empresa.productoservice.domain.exception;

public class PaginaSinProductosException extends RuntimeException {
    public PaginaSinProductosException(int page) {
        super("No hay productos en la página solicitada: " + page);
    }
}