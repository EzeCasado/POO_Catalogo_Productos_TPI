package grupo2.catalogodeproductos_tpi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador Global de Excepciones.
 * La anotación @ControllerAdvice permite a esta clase "interceptar" excepciones
 * lanzadas por cualquier @RestController de la aplicación.
 * * Esto centraliza el manejo de errores y asegura que todas las respuestas de error
 * sigan el mismo formato JSON estándar.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja la excepción 'ResourceNotFoundException' (nuestra excepción personalizada).
     * Se activa cuando un servicio lanza esta excepción (ej. producto no encontrado).
     *
     * @param ex La excepción capturada.
     * @param request La petición web actual.
     * @return un ResponseEntity con el JSON de error y el estado HTTP 404 (Not Found).
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        // Crea el cuerpo del error con el formato estándar
        Map<String, Object> body = new HashMap<>();
        body.put("error", "RecursoNoEncontrado");
        body.put("message", ex.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja la excepción 'SkuAlreadyExistsException' (nuestra excepción personalizada).
     * Se activa cuando se intenta crear un producto con un SKU que ya existe.
     *
     * @param ex La excepción capturada.
     * @param request La petición web actual.
     * @return un ResponseEntity con el JSON de error y el estado HTTP 409 (Conflict).
     */
    @ExceptionHandler(SkuAlreadyExistsException.class)
    public ResponseEntity<Object> handleSkuAlreadyExistsException(
            SkuAlreadyExistsException ex, WebRequest request) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Conflicto");
        body.put("message", ex.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    /**
     * Maneja 'MethodArgumentNotValidException'.
     * Esta excepción se lanza AUTOMÁTICAMENTE por Spring cuando un DTO
     * anotado con @Valid falla la validación (ej. @NotBlank, @Positive).
     *
     * @param ex La excepción de validación capturada.
     * @param request La petición web actual.
     * @return un ResponseEntity con el JSON de error y el estado HTTP 400 (Bad Request).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("error", "PeticionInvalida");
        
        // Extrae el primer error de validación para hacerlo más legible.
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .findFirst() // Toma solo el primer error
                .orElse("Datos de entrada inválidos");
                
        body.put("message", errorMessage);
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Manejador "Atrapa-Todo" para cualquier excepción genérica no controlada.
     * Es la "red de seguridad" final.
     *
     * @param ex La excepción genérica (ej. NullPointerException, etc.).
     * @param request La petición web actual.
     * @return un ResponseEntity con el JSON de error y el estado HTTP 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(
            Exception ex, WebRequest request) {

        // 1. Creamos el cuerpo del error con el formato de la consigna
        Map<String, Object> body = new HashMap<>();
        body.put("error", "ErrorInternoDelServidor");
        body.put("message", "Ocurrió un error inesperado: " + ex.getMessage());

        // 2. Devolvemos el JSON con el código HTTP 500 (Internal Server Error)
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}