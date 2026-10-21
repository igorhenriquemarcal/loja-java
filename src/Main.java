import modelo.Produto;
import modelo.Cliente;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {



        Scanner sc = new Scanner(System.in);

        //Cadastro de produto
        System.out.println("---Cadastro de Produtos---");
        int codigo = lerInt(sc, "Codigo: ");

        System.out.println("Nome: ");
        String nomeProduto  = sc.nextLine();

        double preco = lerDouble(sc, "Preco: ");
        int estoque = lerInt(sc, "Estoque: ");

        Produto produto = new Produto(codigo, nomeProduto, preco, estoque);

        //Cadastro de Cliente

        System.out.println("\n---Cadastro de Clientes---");
        System.out.println("Nome: ");
        String nomeCliente = sc.nextLine();

        System.out.print("CPF: ");
        String cpfCliente = sc.nextLine();

        System.out.print("Email: ");
        String emailCliente = sc.nextLine();

        Cliente cliente = new Cliente(nomeCliente, cpfCliente, emailCliente);

        //Exibição
        System.out.println("\n---Produto cadastrado---");
        System.out.printf("codigo: %d%nNome: %s%nPreco: R$%.2f%nEstoque: %d%n", produto.getCodigo(), produto.getNome(), preco, estoque);

        System.out.println("\n---Cliente cadastrado---");
        System.out.printf("Nome: %s%nCPF: %s%n Email: %s%n", cliente.getNome(), cliente.getCpf(), cliente.getEmail());
    }
    //Metodos para leitura com validação try/cast
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