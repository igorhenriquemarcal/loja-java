package modelo;

public class ServicoPedido {

    // faz toda a finalização e devolve o pedido pronto;
    // se algo der errado, lança exceção e NADA é alterado
    public Pedido finalizar(Carrinho carrinho, Cliente cliente, Pagamento pagamento)
            throws PagamentoRecusadoException, EstoqueInsuficienteException {

        if (carrinho.estaVazio()) {
            throw new IllegalStateException("Carrinho vazio. Adicione produtos antes de finalizar.");
        }

        // 1. valor final (se o pagamento for recusado, o estoque ainda não foi tocado)
        double frete = carrinho.getFrete();
        double valorFinal = pagamento.calcularValorFinal(carrinho.getTotal() + frete);

        // 2. confere o estoque de TODOS os itens antes de alterar qualquer um
        for (ItemCarrinho item : carrinho.getItens()) {
            Produto p = item.getProduto();
            if (item.getQuantidade() > p.getEstoque()) {
                throw new EstoqueInsuficienteException("Estoque insuficiente de " + p.getNome()
                        + ". Disponível: " + p.getEstoque());
            }
        }

        // 3. dá baixa no estoque
        for (ItemCarrinho item : carrinho.getItens()) {
            item.getProduto().baixarEstoque(item.getQuantidade());
        }

        // 4. cria o pedido ANTES de limpar o carrinho
        Pedido pedido = new Pedido(cliente, carrinho.getItens(), frete,
                pagamento.getNome(), valorFinal);
        carrinho.limpar();

        return pedido;
    }
}