package modelo;

public class CartaoCredito implements Pagamento {

    @Override
    public double CalcularValorFinal(double valor){
        return valor *1.03;
    }

    @Override
    public String getNome(){
        return "Cartão de crédito";
    }
}
