package modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarrinhoTest {

    private Carrinho carrinho;
    private Produto fone;
    private Produto camiseta;

    // roda ANTES de cada teste: cada um começa com um carrinho novo e limpo
    @BeforeEach
    void prepararCenario() {
        carrinho = new Carrinho();
        fone = new Eletronico(1, "Fone", 200.0, 5, 12);
        camiseta = new Roupa(2, "Camiseta", 80.0, 20, "M");
    }

    @Test
    void carrinhoNovoComecaVazio() {
        assertTrue(carrinho.estaVazio());
    }

    @Test
    void adicionarProdutoNovoCriaUmItem() throws EstoqueInsuficienteException {
        // ação
        carrinho.adicionar(fone, 2);

        // verificação
        assertEquals(1, carrinho.getItens().size());
        assertEquals(2, carrinho.getQuantidadeDoProduto(1));
    }

    @Test
    void adicionarMesmoProdutoSomaAQuantidade() throws EstoqueInsuficienteException {
        carrinho.adicionar(fone, 2);
        carrinho.adicionar(fone, 1);

        assertEquals(1, carrinho.getItens().size());       // continua um item só
        assertEquals(3, carrinho.getQuantidadeDoProduto(1)); // com 3 unidades
    }

    @Test
    void totalSomaOsSubtotaisDosItens() throws EstoqueInsuficienteException {
        carrinho.adicionar(fone, 1);      // 1 x 200 = 200
        carrinho.adicionar(camiseta, 2);  // 2 x 80  = 160

        assertEquals(360.0, carrinho.getTotal(), 0.001);
    }

    @Test
    void naoAceitaQuantidadeZero() {
        assertThrows(EstoqueInsuficienteException.class,
                () -> carrinho.adicionar(fone, 0));
    }

    @Test
    void naoDeixaPassarDoEstoque() throws EstoqueInsuficienteException {
        carrinho.adicionar(fone, 3); // estoque é 5, então sobram 2

        assertThrows(EstoqueInsuficienteException.class,
                () -> carrinho.adicionar(fone, 3));

        // validar antes de alterar: a tentativa que falhou não pode ter mudado nada
        assertEquals(3, carrinho.getQuantidadeDoProduto(1));
    }

    @Test
    void removerTiraOItemDoCarrinho() throws EstoqueInsuficienteException {
        carrinho.adicionar(fone, 2);

        boolean removeu = carrinho.remover(1);

        assertTrue(removeu);
        assertTrue(carrinho.estaVazio());
    }
}