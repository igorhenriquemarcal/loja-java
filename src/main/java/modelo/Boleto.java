package modelo;

public class Boleto implements Pagamento{

    @Override
    public  double calcularValorFinal(double valor) throws PagamentoRecusadoException {
        return valor;
    }

   @Override
    public String getNome(){
        return "Boleto";
   }
}
