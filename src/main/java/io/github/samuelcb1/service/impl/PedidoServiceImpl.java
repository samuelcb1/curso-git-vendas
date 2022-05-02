package io.github.samuelcb1.service.impl;

import io.github.samuelcb1.domain.entity.Cliente;
import io.github.samuelcb1.domain.entity.ItemPedido;
import io.github.samuelcb1.domain.entity.Pedido;
import io.github.samuelcb1.domain.entity.Produto;
import io.github.samuelcb1.domain.enums.StatusPedido;
import io.github.samuelcb1.domain.repository.Clientes;
import io.github.samuelcb1.domain.repository.ItemsPedido;
import io.github.samuelcb1.domain.repository.Pedidos;
import io.github.samuelcb1.domain.repository.Produtos;
import io.github.samuelcb1.rest.dto.ItemPedidoDTO;
import io.github.samuelcb1.rest.dto.PedidoDTO;
import io.github.samuelcb1.service.PedidoService;
import io.github.samuelcb1.exeption.PedidoNaoEncontradoExeption;
import io.github.samuelcb1.exeption.RegraNegocioExeption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItemsPedido itemsPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioExeption("Codigo de Cliente invalido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.RELIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        repository.save(pedido);
        itemsPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus( Integer id, StatusPedido statusPedido) {
        repository
                .findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusPedido);
                    return repository.save(pedido);
                }).orElseThrow( () -> new PedidoNaoEncontradoExeption() );
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items) {
        if (items.isEmpty()) {
            throw new RegraNegocioExeption("Não e possivel realizar pedido sem items");
        }

        return items
                .stream()
                .map(dto -> {
                    Integer idproduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idproduto)
                            .orElseThrow(() -> new RegraNegocioExeption("Codigo de Produto invalido: " + idproduto
                            ));


                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());
    }
}
