package io.github.samuelcb1.service;

import io.github.samuelcb1.domain.entity.Pedido;
import io.github.samuelcb1.domain.enums.StatusPedido;
import io.github.samuelcb1.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);
    Optional<Pedido> obterPedidoCompleto(Integer id);
    void atualizaStatus (Integer id, StatusPedido statusPedido);
}
