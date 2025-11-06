package grupo2.catalogodeproductos_tpi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;


/**
 * Esta anotación le dice a Spring que esta clase
 * "vigilará" a todos los Controladores (@RestController).
 */
@ControllerAdvice

public class GlobalExceptionHandler {


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
