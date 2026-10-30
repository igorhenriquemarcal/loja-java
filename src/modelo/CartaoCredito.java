package modelo;

public class CartaoCredito implements Pagamento {

    @Override
    public double calcularValorFinal(double valor) throws PagamentoRecusadoException {
        return valor *1.03;
    }

    @Override
    public String getNome(){
        return "Cartão de crédito";
    }
}
