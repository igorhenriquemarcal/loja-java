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
}
