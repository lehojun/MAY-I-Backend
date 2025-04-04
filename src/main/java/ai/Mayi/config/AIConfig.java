package ai.Mayi.config;


import lombok.Getter;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AIConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HttpHeaders gptHeaders() {
        HttpHeaders headers = new HttpHeaders(); //headers.set()은 service 로직에서 구현
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
