package modelo;

public class Boleto implements Pagamento{

    @Override
    public  double calcularValorFinal(double valor) {
        return valor;
    }

   @Override
    public String getNome(){
        return "Boleto";
   }
}
