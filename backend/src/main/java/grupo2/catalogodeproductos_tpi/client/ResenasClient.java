package grupo2.catalogodeproductos_tpi.client;


import grupo2.catalogodeproductos_tpi.dto.RatingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ResenasClient {

    @Autowired
    private RestTemplate restTemplate;

    private final String RESENAS_API_URL = "https://poo2025.unsada.edu.ar:{puerto}";

    private RatingDTO getRatingDTO(String sku){

        String url = RESENAS_API_URL + sku + "/rating";

        try{

            RatingDTO respuesta = restTemplate.getForObject(url, RatingDTO.class);

            return respuesta;

        }catch(Exception e){

            return null;

        }

    }
}
