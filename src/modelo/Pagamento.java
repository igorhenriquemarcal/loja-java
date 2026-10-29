package modelo;

public interface Pagamento {
    double calcularValorFinal(double valor);

    String getNome();
}
