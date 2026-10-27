package modelo;

public class Alimento extends Produto{

    private String dataValidade;

    public Alimento(int codigo, String nome, double preco, int estoque, String dataValidade){
        super(codigo, nome, preco, estoque);
        this.dataValidade = dataValidade;
    }

    public String getDataValidade() {
        return dataValidade;
    }

    @Override
    public String descricaoExtra(){
        return "Validade: " + dataValidade;
    }

    @Override
    public double calcularFrete(){
        return getPreco() * 0.10;
    }
}
