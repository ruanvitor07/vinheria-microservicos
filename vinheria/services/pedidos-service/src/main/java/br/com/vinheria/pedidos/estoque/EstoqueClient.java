package br.com.vinheria.pedidos.estoque;

import br.com.vinheria.pedidos.pedido.dto.ItemPedidoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EstoqueClient {

    private final RestClient estoqueRestClient;
    private final ObjectMapper objectMapper;

    public ReservaResponse reservar(Long pedidoId, List<ItemPedidoRequest> itens, String token) {
        try {
            return estoqueRestClient.post()
                    .uri("/api/reservas")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ReservaRequest(pedidoId, itens))
                    .retrieve()
                    .body(ReservaResponse.class);
        } catch (HttpClientErrorException e) {          // 4xx -> regra de negócio
            throw new EstoqueRecusouException(extrairMensagem(e));
        } catch (RestClientException e) {               // timeout, 5xx, conexão...
            throw new EstoqueIndisponivelException(e);
        }
    }

    public void liberarReserva(Long pedidoId, String token) {
        try {
            estoqueRestClient.delete()
                    .uri("/api/reservas/{pedidoId}", pedidoId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            throw new EstoqueRecusouException(extrairMensagem(e));
        } catch (RestClientException e) {
            throw new EstoqueIndisponivelException(e);
        }
    }

    /** O Estoque responde erros no formato ProblemDetail; o texto útil está no campo "detail". */
    private String extrairMensagem(HttpClientErrorException e) {
        try {
            String detalhe = objectMapper.readTree(e.getResponseBodyAsString()).path("detail").asText();
            return detalhe.isBlank() ? "Estoque recusou a operação (HTTP " + e.getStatusCode().value() + ")" : detalhe;
        } catch (Exception ignorada) {
            return "Estoque recusou a operação (HTTP " + e.getStatusCode().value() + ")";
        }
    }
}
