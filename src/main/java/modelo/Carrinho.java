package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Carrinho {

    private List<ItemCarrinho> itens = new ArrayList<>();

    // se o produto já está no carrinho, soma a quantidade
    public void adicionar(Produto produto, int quantidade) {
        for (ItemCarrinho item : itens) {
            if (item.getProduto().getCodigo() == produto.getCodigo()) {
                item.somarQuantidade(quantidade);
                return;
            }
        }
        itens.add(new ItemCarrinho(produto, quantidade));
    }

    // devolve true se removeu, false se não achou
    public boolean remover(int codigo) {
        for (int i = 0; i < itens.size(); i++) {
            if (itens.get(i).getProduto().getCodigo() == codigo) {
                itens.remove(i);
                return true;
            }
        }
        return false;
    }

    public double getTotal() {
        double total = 0;
        for (ItemCarrinho item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

    public List<ItemCarrinho> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public boolean estaVazio() {
        return itens.isEmpty();
    }

    public void limpar() {
        itens.clear();
    }

    // quantas unidades desse produto já estão no carrinho
    public int getQuantidadeDoProduto(int codigo) {
        for (ItemCarrinho item : itens) {
            if (item.getProduto().getCodigo() == codigo) {
                return item.getQuantidade();
            }
        }
        return 0;
    }

    // soma o frete de cada linha do carrinho (um frete por produto diferente)
    public double getFrete() {
        double frete = 0;
        for (ItemCarrinho item : itens) {
            frete += item.getProduto().calcularFrete();
        }
        return frete;
    }
}