package grupo2.catalogodeproductos_tpi.exception;

/**
 * Excepción de runtime personalizada para señalar un intento de crear un recurso
 * que viola una restricción de unicidad (en este caso, el SKU).
 *
 * Será capturada por el GlobalExceptionHandler para devolver un código HTTP 409 (Conflict).
 */
public class SkuAlreadyExistsException extends RuntimeException {

    /**
     * Constructor que recibe el mensaje de error detallado.
     * @param message El mensaje que describe el conflicto.
     */
    public SkuAlreadyExistsException(String message) {
        super(message);
    }
}