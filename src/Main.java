import modelo.*;
import java.util.ArrayList;
import java.util.List;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Catalogo catalogo = new Catalogo();
        Carrinho carrinho = new Carrinho();
        List<Pedido> pedidos = new ArrayList<>();
        int opcao;

        do {
            mostrarMenu();
            opcao = lerInt(sc, "Opção: ");

            switch (opcao) {
                case 1:
                    cadastrarProduto(sc, catalogo);
                    break;
                case 2:
                    listarCatalogo(catalogo);
                    break;
                case 3:
                    adicionarAoCarrinho(sc, catalogo, carrinho);
                    break;
                case 4:
                    verCarrinho(carrinho);
                    break;
                case 5:
                    finalizarPedido(sc, carrinho, pedidos);
                    break;
                case 6:
                    mostrarHistorico(pedidos);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    // ---------- Menu ----------
    public static void mostrarMenu() {
        System.out.println("\n===== LOJA =====");
        System.out.println("1 - Cadastrar produto no catálogo");
        System.out.println("2 - Listar catálogo");
        System.out.println("3 - Adicionar ao carrinho");
        System.out.println("4 - Ver carrinho");
        System.out.println("5 - Finalizar pedido");
        System.out.println("6 - Histórico de pedidos");
        System.out.println("0 - Sair");
    }

    // ---------- Opção 1 ----------
    public static void cadastrarProduto(Scanner sc, Catalogo catalogo) {
        System.out.println("\n---Cadastro de Produto---");
        System.out.println("1 - Eletrônico");
        System.out.println("2 - Roupa");
        System.out.println("3 - Alimento");
        int tipo = lerInt(sc, "Tipo: ");

        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo inválido.");
            return;
        }

        int codigo = lerInt(sc, "Código: ");
        if (catalogo.buscar(codigo) != null) {
            System.out.println("Já existe um produto com o código " + codigo + ".");
            return;
        }

        System.out.print("Nome: ");
        String nome = sc.nextLine();
        double preco = lerDouble(sc, "Preço: ");
        int estoque = lerInt(sc, "Estoque: ");

        Produto produto;

        switch (tipo) {
            case 1:
                int garantia = lerInt(sc, "Garantia (meses): ");
                produto = new Eletronico(codigo, nome, preco, estoque, garantia);
                break;
            case 2:
                System.out.print("Tamanho (P, M, G): ");
                String tamanho = sc.nextLine();
                produto = new Roupa(codigo, nome, preco, estoque, tamanho);
                break;
            default:
                System.out.print("Data de validade: ");
                String validade = sc.nextLine();
                produto = new Alimento(codigo, nome, preco, estoque, validade);
                break;
        }

        catalogo.adicionar(produto);
        System.out.println("Produto cadastrado!");
    }

    // ---------- Opção 2 ----------
    public static void listarCatalogo(Catalogo catalogo) {
        System.out.println("\n---Catálogo---");

        if (catalogo.listar().isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        for (Produto p : catalogo.listar()) {
            System.out.printf("[%d] %s | R$ %.2f | estoque: %d | %s%n",
                    p.getCodigo(), p.getNome(), p.getPreco(), p.getEstoque(), p.descricaoExtra());
        }
    }

    // ---------- Opção 3 ----------
    public static void adicionarAoCarrinho(Scanner sc, Catalogo catalogo, Carrinho carrinho) {
        System.out.println("\n---Adicionar ao carrinho---");

        int codigo = lerInt(sc, "Código do produto: ");
        Produto produto = catalogo.buscar(codigo);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        int quantidade = lerInt(sc, "Quantidade: ");
        if (quantidade <= 0) {
            System.out.println("A quantidade deve ser maior que zero.");
            return;
        }

        int jaNoCarrinho = carrinho.getQuantidadeDoProduto(codigo);
        int disponivel = produto.getEstoque() - jaNoCarrinho;

        if (quantidade > disponivel) {
            System.out.println("Estoque insuficiente. Você pode adicionar no máximo "
                    + disponivel + " unidade(s).");
            return;
        }

        carrinho.adicionar(produto, quantidade);
        System.out.println(quantidade + "x " + produto.getNome() + " adicionado(s) ao carrinho.");
    }

    // ---------- Opção 4 ----------
    public static void verCarrinho(Carrinho carrinho) {
        System.out.println("\n---Carrinho---");

        if (carrinho.estaVazio()) {
            System.out.println("Carrinho vazio.");
            return;
        }

        for (ItemCarrinho item : carrinho.getItens()) {
            System.out.printf("%dx %s | R$ %.2f cada | subtotal: R$ %.2f%n",
                    item.getQuantidade(), item.getProduto().getNome(),
                    item.getProduto().getPreco(), item.getSubtotal());
        }

        System.out.printf("Produtos: R$ %.2f%n", carrinho.getTotal());
        System.out.printf("Frete:    R$ %.2f%n", carrinho.getFrete());
        System.out.printf("Total:    R$ %.2f%n", carrinho.getTotal() + carrinho.getFrete());
    }

    // ---------- Opção 5 ----------
    public static void finalizarPedido(Scanner sc, Carrinho carrinho, List<Pedido> pedidos) {
        System.out.println("\n---Finalizar pedido---");

        if (carrinho.estaVazio()) {
            System.out.println("Carrinho vazio. Adicione produtos antes de finalizar.");
            return;
        }

        // 1. dados do cliente
        System.out.print("Nome do cliente: ");
        String nome = sc.nextLine();
        System.out.print("CPF: ");
        String cpf = sc.nextLine();
        System.out.print("E-mail: ");
        String email = sc.nextLine();
        Cliente cliente = new Cliente(nome, cpf, email);

        // 2. forma de pagamento
        Pagamento pagamento = escolherPagamento(sc);
        if (pagamento == null) {
            System.out.println("Forma de pagamento inválida. Pedido cancelado.");
            return;
        }

        // 3. valor final (se o pagamento for recusado, o estoque ainda não foi tocado)
        double frete = carrinho.getFrete();
        double totalComFrete = carrinho.getTotal() + frete;
        double valorFinal;

        try {
            valorFinal = pagamento.calcularValorFinal(totalComFrete);
        } catch (PagamentoRecusadoException e) {
            System.out.println("Pagamento recusado: " + e.getMessage());
            return;
        }

        // 4. confere o estoque de TODOS os itens antes de alterar qualquer um
        for (ItemCarrinho item : carrinho.getItens()) {
            if (item.getQuantidade() > item.getProduto().getEstoque()) {
                System.out.println("Estoque insuficiente de " + item.getProduto().getNome()
                        + ". Disponível: " + item.getProduto().getEstoque());
                return;
            }
        }

        // 5. dá baixa no estoque
        for (ItemCarrinho item : carrinho.getItens()) {
            try {
                item.getProduto().baixarEstoque(item.getQuantidade());
            } catch (EstoqueInsuficienteException e) {
                // não deve acontecer, pois já conferimos acima
                System.out.println("Erro inesperado: " + e.getMessage());
                return;
            }
        }

        // 6. registra o pedido ANTES de limpar o carrinho
        Pedido pedido = new Pedido(cliente, carrinho.getItens(), frete,
                pagamento.getNome(), valorFinal);
        pedidos.add(pedido);
        carrinho.limpar();

        System.out.println("\nPedido finalizado com sucesso!");
        System.out.printf("Pago com %s: R$ %.2f%n", pedido.getFormaPagamento(), pedido.getValorFinal());
    }

    // devolve null se a opção for inválida
    public static Pagamento escolherPagamento(Scanner sc) {
        System.out.println("\nForma de pagamento:");
        System.out.println("1 - Pix");
        System.out.println("2 - Cartão de crédito");
        System.out.println("3 - Boleto");
        int opcao = lerInt(sc, "Opção: ");

        switch (opcao) {
            case 1:
                return new Pix();
            case 2:
                return new CartaoCredito();
            case 3:
                return new Boleto();
            default:
                return null;
        }
    }

    // ---------- Opção 6 ----------
    public static void mostrarHistorico(List<Pedido> pedidos) {
        System.out.println("\n---Histórico de pedidos---");

        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido finalizado.");
            return;
        }

        int numero = 1;
        for (Pedido pedido : pedidos) {
            System.out.printf("%nPedido #%d - %s (%s)%n",
                    numero, pedido.getCliente().getNome(), pedido.getCliente().getEmail());

            for (ItemCarrinho item : pedido.getItens()) {
                System.out.printf("  %dx %s | subtotal: R$ %.2f%n",
                        item.getQuantidade(), item.getProduto().getNome(), item.getSubtotal());
            }

            System.out.printf("  Produtos: R$ %.2f | Frete: R$ %.2f%n",
                    pedido.getSubtotal(), pedido.getFrete());
            System.out.printf("  Pago com %s: R$ %.2f%n",
                    pedido.getFormaPagamento(), pedido.getValorFinal());
            numero++;
        }
    }

    // ---------- Leitura com validação ----------
    public static int lerInt(Scanner sc, String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                int valor = sc.nextInt();
                sc.nextLine();
                return valor;
            } catch (Exception e) {
                System.out.println("Informação inválida, tente novamente.");
                sc.nextLine();
            }
        }
    }

    public static double lerDouble(Scanner sc, String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                double valor = sc.nextDouble();
                sc.nextLine();
                return valor;
            } catch (Exception e) {
                System.out.println("Informação inválida, tente novamente.");
                sc.nextLine();
            }
        }
    }
}