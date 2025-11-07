package grupo2.catalogodeproductos_tpi.client;


import grupo2.catalogodeproductos_tpi.dto.RatingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente HTTP para comunicarse con el Módulo 7 (Reseñas y Reputación).
 * Se encarga de obtener el rating promedio de los productos.
 */
@Component
public class ResenasClient {

    @Autowired
    private RestTemplate restTemplate;

    // Lee la URL desde application.properties
    @Value("${api.resenas.url}")
    private String RESENAS_API_URL;


    /**
     * Llama al Módulo 7 para obtener el rating promedio y la cantidad de reseñas de un SKU.
     *
     * @param sku El SKU (String) del producto a consultar.
     * @return un RatingDTO con la información del rating. Devuelve null si la API falla o no hay reseñas.
     */
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
