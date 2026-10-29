package modelo;

public class Pix implements Pagamento {

    @Override
    public double calcularValorFinal(double valor) {
        return valor * 0.95;   // 5% de desconto
    }

    @Override
    public String getNome() {
        return "Pix";
    }
}