package grupo2.catalogodeproductos_tpi.exception;

/**
 * Excepción de runtime personalizada para señalar que un recurso solicitado no se encontró.
 * Al ser una 'RuntimeException', no estamos obligados a capturarla (unchecked exception),
 * lo cual limpia el código.
 * * Será capturada por el GlobalExceptionHandler para devolver un código HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Constructor que recibe el mensaje de error detallado.
     * @param message El mensaje que describe por qué no se encontró el recurso.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}