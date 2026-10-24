package modelo;

public class Eletronico extends Produto {

    private int garantiaMeses;

    public Eletronico(int codigo, String nome, double preco, int estoque, int garantiaMeses){
        super(codigo, nome, preco, estoque);
        this.garantiaMeses = garantiaMeses;

    }

    public int getGarantiaMeses() {
        return garantiaMeses;
    }

}
