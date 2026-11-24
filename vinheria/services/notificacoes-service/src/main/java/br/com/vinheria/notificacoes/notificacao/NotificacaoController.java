package br.com.vinheria.notificacoes.notificacao;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoRepository repository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Notificacao> ultimas() {
        return repository.findTop50ByOrderByIdDesc();
    }
}
