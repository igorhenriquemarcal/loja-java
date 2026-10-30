import modelo.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Catalogo catalogo = new Catalogo();
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