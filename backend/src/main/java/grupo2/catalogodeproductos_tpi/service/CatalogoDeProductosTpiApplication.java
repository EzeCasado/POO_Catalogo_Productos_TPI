package grupo2.catalogodeproductos_tpi.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CatalogoDeProductosTpiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogoDeProductosTpiApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
