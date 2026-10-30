package modelo;

public class ItemCarrinho {

    private Produto produto;
    private int quantidade;

    public ItemCarrinho(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void somarQuantidade(int quantidade) {
        this.quantidade += quantidade;
    }

    public double getSubtotal() {
        return produto.getPreco() * quantidade;
    }
}