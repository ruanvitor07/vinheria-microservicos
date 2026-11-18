package br.com.vinheria.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway: único ponto de entrada. Apenas roteia por caminho; não contém regra de negócio.
 * A validação do JWT é feita por cada serviço (defesa em profundidade).
 */
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
