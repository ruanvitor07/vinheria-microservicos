package br.com.vinheria.pedidos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class EstoqueClientConfig {

    /** RestClient com TIMEOUTS: uma chamada lenta não deve travar o serviço de Pedidos. */
    @Bean
    RestClient estoqueRestClient(@Value("${app.estoque.url}") String baseUrl) {
        var fabrica = new SimpleClientHttpRequestFactory();
        fabrica.setConnectTimeout(2000);
        fabrica.setReadTimeout(3000);
        return RestClient.builder().baseUrl(baseUrl).requestFactory(fabrica).build();
    }
}
