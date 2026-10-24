import modelo.Alimento;
import modelo.Cliente;
import modelo.Eletronico;
import modelo.Produto;
import modelo.Roupa;

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
        if (produto instanceof Eletronico) {
            Eletronico e = (Eletronico) produto;
            System.out.println("Garantia: " + e.getGarantiaMeses() + " meses");
        } else if (produto instanceof Roupa) {
            Roupa r = (Roupa) produto;
            System.out.println("Tamanho: " + r.getTamanho());
        } else if (produto instanceof Alimento) {
            Alimento a = (Alimento) produto;
            System.out.println("Validade: " + a.getDataValidade());
        }

        System.out.println("\n---Cliente cadastrado---");
        System.out.printf("Nome: %s%nCPF: %s%nEmail: %s%n",
                cliente.getNome(), cliente.getCpf(), cliente.getEmail());
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