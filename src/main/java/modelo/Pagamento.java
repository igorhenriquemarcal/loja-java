package modelo;

public interface Pagamento {
    double calcularValorFinal(double valor) throws PagamentoRecusadoException;

    String getNome();
}
