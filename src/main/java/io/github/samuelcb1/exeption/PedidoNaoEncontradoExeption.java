package io.github.samuelcb1.exeption;

public class PedidoNaoEncontradoExeption extends RuntimeException{
    public PedidoNaoEncontradoExeption() {
        super("Pedido não encontrado");
    }
}
