package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {

    private Cliente cliente;
    private List<ItemCarrinho> itens;
    private double subtotal;
    private double frete;
    private String formaPagamento;
    private double valorFinal;

    public Pedido(Cliente cliente, List<ItemCarrinho> itens, double frete,
                  String formaPagamento, double valorFinal) {
        this.cliente = cliente;
        this.itens = new ArrayList<>(itens);   // cópia
        this.frete = frete;
        this.formaPagamento = formaPagamento;
        this.valorFinal = valorFinal;

        double soma = 0;
        for (ItemCarrinho item : this.itens) {
            soma += item.getSubtotal();
        }
        this.subtotal = soma;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemCarrinho> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getFrete() {
        return frete;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public double getValorFinal() {
        return valorFinal;
    }
}