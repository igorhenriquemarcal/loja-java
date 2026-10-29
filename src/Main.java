import modelo.Alimento;
import modelo.Cliente;
import modelo.Eletronico;
import modelo.Produto;
import modelo.Roupa;
import java.util.ArrayList;
import modelo.Pagamento;
import modelo.Pix;
import modelo.CartaoCredito;
import modelo.Boleto;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // ---------- Cadastro de produto ----------
        System.out.println("---Cadastro de Produtos---");
        System.out.println("1 - Eletrônico");
        System.out.println("2 - Roupa");
        System.out.println("3 - Alimento");
        int tipo = lerInt(sc, "Tipo: ");

        int codigo = lerInt(sc, "Código: ");

        System.out.print("Nome: ");
        String nomeProduto = sc.nextLine();

        double preco = lerDouble(sc, "Preço: ");
        int estoque = lerInt(sc, "Estoque: ");

        Produto produto;   // variável do tipo PAI

        switch (tipo) {
            case 1:
                int garantia = lerInt(sc, "Garantia (meses): ");
                produto = new Eletronico(codigo, nomeProduto, preco, estoque, garantia);
                break;
            case 2:
                System.out.print("Tamanho (P, M, G): ");
                String tamanho = sc.nextLine();
                produto = new Roupa(codigo, nomeProduto, preco, estoque, tamanho);
                break;
            case 3:
                System.out.print("Data de validade: ");
                String validade = sc.nextLine();
                produto = new Alimento(codigo, nomeProduto, preco, estoque, validade);
                break;
            default:
                System.out.println("Tipo inválido. Encerrando.");
                return;
        }

        // ---------- Cadastro de cliente ----------
        System.out.println("\n---Cadastro de Clientes---");
        System.out.print("Nome: ");
        String nomeCliente = sc.nextLine();

        System.out.print("CPF: ");
        String cpfCliente = sc.nextLine();

        System.out.print("Email: ");
        String emailCliente = sc.nextLine();

        Cliente cliente = new Cliente(nomeCliente, cpfCliente, emailCliente);

        // ---------- Exibição ----------
        System.out.println("\n---Produto cadastrado---");
        System.out.printf("Código: %d%nNome: %s%nPreço: R$%.2f%nEstoque: %d%n",
                produto.getCodigo(), produto.getNome(), produto.getPreco(), produto.getEstoque());

        // dado exclusivo de cada tipo
        System.out.println(produto.descricaoExtra());
        System.out.printf("Frete: R$ %.2f%n", produto.calcularFrete());
        System.out.printf("Total: R$ %.2f%n", produto.getPreco() + produto.calcularFrete());

        System.out.println("\n---Cliente cadastrado---");
        System.out.printf("Nome: %s%nCPF: %s%nEmail: %s%n",
                cliente.getNome(), cliente.getCpf(), cliente.getEmail());


        System.out.println("\n---Catálogo de exemplo---");

        ArrayList<Produto> produtos = new ArrayList<>();
        produtos.add(new Eletronico(1, "Fone", 200.0, 5, 12));
        produtos.add(new Roupa(2, "Camiseta", 80.0, 20, "M"));
        produtos.add(new Alimento(3, "Arroz 5kg", 30.0, 50, "31/12/2026"));

        for (Produto p : produtos) {
            System.out.println(p.getNome() + " - " + p.descricaoExtra());
            System.out.printf("Frete: R$ %.2f%n", p.calcularFrete());
        }

        // ---------- Pagamento ----------
        System.out.println("\n---Forma de pagamento---");
        System.out.println("1 - Pix");
        System.out.println("2 - Cartão de crédito");
        System.out.println("3 - Boleto");
        int opcaoPagamento = lerInt(sc, "Opção: ");

        Pagamento pagamento;   // tipo da INTERFACE

        switch (opcaoPagamento) {
            case 1:
                pagamento = new Pix();
                break;
            case 2:
                pagamento = new CartaoCredito();
                break;
            case 3:
                pagamento = new Boleto();
                break;
            default:
                System.out.println("Opção inválida. Encerrando.");
                return;
        }

        double total = produto.getPreco() + produto.calcularFrete();
        System.out.printf("Pagando com %s: R$ %.2f%n",
                pagamento.getNome(), pagamento.calcularValorFinal(total));
        System.out.printf("Pagando com %s: R$ %.2f%n",
                pagamento.getNome(), pagamento.calcularValorFinal(total));

        System.out.println("\n---Comparativo de pagamentos---");

        ArrayList<Pagamento> formas = new ArrayList<>();
        formas.add(new Pix());
        formas.add(new CartaoCredito());
        formas.add(new Boleto());

        for (Pagamento f : formas) {
            System.out.printf("%s: R$ %.2f%n", f.getNome(), f.calcularValorFinal(total));
        }
    }

    // Métodos para leitura com validação try/catch
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