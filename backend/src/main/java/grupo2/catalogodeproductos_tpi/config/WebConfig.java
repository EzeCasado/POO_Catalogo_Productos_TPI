package grupo2.catalogodeproductos_tpi.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration le dice a Spring que esto es una clase de configuración
@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Autowired
    private ApiKeyAuthInterceptor apiKeyAuthInterceptor;

    // Sobrescribimos este método para "registrar" a nuestro guardia
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(apiKeyAuthInterceptor);

    }
}
