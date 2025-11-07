package grupo2.catalogodeproductos_tpi.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class UsuarioClient {


    @Autowired
    private RestTemplate restTemplate;

    private final String USUARIO_API_URL ="https://poo2025.unsada.edu.ar:{puerto} ";

    public boolean validarToken(String apiKey){

        //Headers de la petición

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);
        headers.set("Content-Type", "application/json");

        Map<String,String> body = new HashMap<>();
        body.put("servicio","catalogo");

        HttpEntity<Map<String,String>> request = new HttpEntity<>(body, headers);

        try{

            ResponseEntity  <String> response = restTemplate.postForEntity(USUARIO_API_URL, request, String.class);

            return response.getStatusCode() == HttpStatus.OK;


        } catch(Exception e){

            return false;

        }

    }

}
