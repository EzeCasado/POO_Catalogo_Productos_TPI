package grupo2.catalogodeproductos_tpi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;


/**
 * Manejador Global de Excepciones (@ControllerAdvice).
 * Captura excepciones lanzadas por cualquier Controlador
 * [cite_start]y las formatea en el JSON de error estándar requerido. [cite: 46, 50, 59]
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Manejador "Atrapa-Todo" para cualquier excepción no controlada (Errores 500).
     *
     * @param ex La excepción lanzada.
     * @param request La petición web actual.
     * @return un ResponseEntity con el JSON de error y el estado HTTP 500.
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
