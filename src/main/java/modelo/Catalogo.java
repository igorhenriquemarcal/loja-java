package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Catalogo {

    private Map<Integer, Produto> produtos = new HashMap<>();

    public boolean adicionar(Produto produto) {
        if (produtos.containsKey(produto.getCodigo())) {
            return false;
        }
        produtos.put(produto.getCodigo(), produto);
        return true;
    }

    public Produto buscar(int codigo) {
        return produtos.get(codigo);
    }

    public List<Produto> listar() {
        return new ArrayList<>(produtos.values());
    }

}