package grupo2.catalogodeproductos_tpi.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class ApiKeyAuthInterceptor implements HandlerInterceptor {

    /**
     * Este método se ejecuta ANTES de que la petición llegue
     * al controlador.
     */

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String method = request.getMethod();

        if (method.equalsIgnoreCase("GET")) {

            return true;

        }

        String apiKey = request.getHeader("X-Api-Key");

        if (apiKey == null || apiKey.trim().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.setContentType("application/json");

            response.getWriter().write("{\"error\": \"NoAutorizado\", \"message\": \"Se requiere un X-API-Key para esta operación\"}");

            return false;

        }

        return true;


    }

}
