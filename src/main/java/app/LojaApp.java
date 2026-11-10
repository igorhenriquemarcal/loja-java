package app;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.*;

public class LojaApp extends Application {

    private final Catalogo catalogo = new Catalogo();
    private final Carrinho carrinho = new Carrinho();
    private final List<Pedido> pedidos = new ArrayList<>();
    private final ServicoPedido servicoPedido = new ServicoPedido();

    // atributo para os botões do menu conseguirem trocar o centro da tela
    private BorderPane raiz;

    @Override
    public void start(Stage stage) {
        carregarDadosDeExemplo();

        raiz = new BorderPane();
        raiz.setTop(criarTopo());
        raiz.setLeft(criarMenuLateral());
        raiz.setCenter(criarAreaCatalogo());
        raiz.setBottom(criarRodape());

        Scene cena = new Scene(raiz, 900, 600);

        stage.setTitle("Loja Java");
        stage.setScene(cena);
        stage.show();
    }

    // dados temporários, só para a tela ter o que mostrar
    private void carregarDadosDeExemplo() {
        catalogo.adicionar(new Eletronico(1, "Fone", 200.0, 5, 12));
        catalogo.adicionar(new Roupa(2, "Camiseta", 80.0, 20, "M"));
        catalogo.adicionar(new Alimento(3, "Arroz 5kg", 30.0, 50, "31/12/2026"));
    }

    // ---------- Topo ----------
    private Node criarTopo() {
        Label titulo = new Label("Loja Java");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        HBox topo = new HBox(titulo);
        topo.setPadding(new Insets(15));
        return topo;
    }

    // ---------- Menu lateral ----------
    private Node criarMenuLateral() {
        Button btnCatalogo = new Button("Catálogo");
        Button btnCarrinho = new Button("Carrinho");
        Button btnPedidos = new Button("Pedidos");

        // faz cada botão ocupar toda a largura do menu
        btnCatalogo.setMaxWidth(Double.MAX_VALUE);
        btnCarrinho.setMaxWidth(Double.MAX_VALUE);
        btnPedidos.setMaxWidth(Double.MAX_VALUE);

        // o que acontece no clique: troca o conteúdo do centro
        btnCatalogo.setOnAction(e -> raiz.setCenter(criarAreaCatalogo()));
        btnCarrinho.setOnAction(e -> raiz.setCenter(criarAreaCarrinho()));

        VBox menu = new VBox(10, btnCatalogo, btnCarrinho, btnPedidos);
        menu.setPadding(new Insets(15));
        menu.setPrefWidth(170);
        return menu;
    }

    // ---------- Centro: catálogo ----------
    private Node criarAreaCatalogo() {
        Label subtitulo = new Label("Catálogo");
        subtitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField campoBusca = new TextField();
        campoBusca.setPromptText("Buscar produto...");

        // a lista guarda os próprios produtos, não texto
        ListView<Produto> lista = new ListView<>();
        lista.setPlaceholder(new Label("Nenhum produto encontrado"));

        // define como cada produto aparece na lista
        lista.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Produto p, boolean vazio) {
                super.updateItem(p, vazio);
                if (vazio || p == null) {
                    setText(null);
                } else {
                    setText(String.format("[%d] %s | R$ %.2f | estoque: %d | %s",
                            p.getCodigo(), p.getNome(), p.getPreco(), p.getEstoque(), p.descricaoExtra()));
                }
            }
        });

        lista.getItems().setAll(catalogo.listar());

        // a cada tecla, refaz a lista só com os produtos que batem
        campoBusca.textProperty().addListener((obs, textoAntigo, textoNovo) ->
                lista.getItems().setAll(catalogo.buscarPorNome(textoNovo)));

        // ---- adicionar ao carrinho ----
        Spinner<Integer> spnQuantidade = new Spinner<>(1, 99, 1);
        spnQuantidade.setPrefWidth(80);

        Button btnAdicionar = new Button("Adicionar ao carrinho");
        Label lblMensagem = new Label();

        btnAdicionar.setOnAction(e -> {
            Produto selecionado = lista.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                lblMensagem.setText("Selecione um produto na lista.");
                return;
            }

            int quantidade = spnQuantidade.getValue();
            int jaNoCarrinho = carrinho.getQuantidadeDoProduto(selecionado.getCodigo());

            // valida antes de alterar: não deixa passar do estoque
            if (jaNoCarrinho + quantidade > selecionado.getEstoque()) {
                lblMensagem.setText(String.format("Estoque insuficiente: %d disponíveis, %d já no carrinho.",
                        selecionado.getEstoque(), jaNoCarrinho));
                return;
            }

            carrinho.adicionar(selecionado, quantidade);
            lblMensagem.setText(String.format("%dx %s adicionado ao carrinho.",
                    quantidade, selecionado.getNome()));
        });

        HBox acoes = new HBox(10, new Label("Qtd:"), spnQuantidade, btnAdicionar, lblMensagem);
        acoes.setAlignment(Pos.CENTER_LEFT);

        VBox area = new VBox(10, subtitulo, campoBusca, lista, acoes);
        VBox.setVgrow(lista, Priority.ALWAYS);   // a lista ocupa o espaço que sobrar
        area.setPadding(new Insets(15));
        return area;
    }

    // ---------- Centro: carrinho ----------
    private Node criarAreaCarrinho() {
        Label subtitulo = new Label("Carrinho");
        subtitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // a lista guarda os itens do carrinho, não texto
        ListView<ItemCarrinho> lista = new ListView<>();
        lista.setPlaceholder(new Label("Carrinho vazio"));

        // define como cada item aparece na lista
        lista.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ItemCarrinho item, boolean vazio) {
                super.updateItem(item, vazio);
                if (vazio || item == null) {
                    setText(null);
                } else {
                    Produto p = item.getProduto();
                    setText(String.format("%dx %s | R$ %.2f cada | subtotal: R$ %.2f",
                            item.getQuantidade(), p.getNome(), p.getPreco(), item.getSubtotal()));
                }
            }
        });

        lista.getItems().setAll(carrinho.getItens());

        // ---- remover do carrinho ----
        Button btnRemover = new Button("Remover item");
        Label lblMensagem = new Label();

        btnRemover.setOnAction(e -> {
            ItemCarrinho selecionado = lista.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                lblMensagem.setText("Selecione um item para remover.");
                return;
            }

            carrinho.remover(selecionado.getProduto().getCodigo());

            // remonta a tela do carrinho com a lista e os totais atualizados
            raiz.setCenter(criarAreaCarrinho());
        });

        HBox acoes = new HBox(10, btnRemover, lblMensagem);
        acoes.setAlignment(Pos.CENTER_LEFT);

        // ---- resumo de valores ----
        Label lblSubtotal = new Label(String.format("Subtotal: R$ %.2f", carrinho.getTotal()));
        Label lblFrete = new Label(String.format("Frete: R$ %.2f", carrinho.getFrete()));
        Label lblTotal = new Label(String.format("Total: R$ %.2f",
                carrinho.getTotal() + carrinho.getFrete()));
        lblTotal.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        VBox resumo = new VBox(5, lblSubtotal, lblFrete, lblTotal);

        // ---- finalizar pedido ----
        Label tituloFinalizar = new Label("Finalizar pedido");
        tituloFinalizar.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do cliente");
        TextField txtCpf = new TextField();
        txtCpf.setPromptText("CPF");
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("E-mail");

        HBox linhaCliente = new HBox(10, txtNome, txtCpf, txtEmail);

        ComboBox<String> cmbPagamento = new ComboBox<>();
        cmbPagamento.getItems().addAll("Pix", "Cartão de crédito", "Boleto");
        cmbPagamento.setPromptText("Forma de pagamento");

        Button btnFinalizar = new Button("Finalizar pedido");
        Label lblMensagemPedido = new Label();

        btnFinalizar.setOnAction(e -> {
            // cláusulas de guarda: confere tudo que a tela é responsável por conferir
            if (carrinho.estaVazio()) {
                lblMensagemPedido.setText("Carrinho vazio. Adicione produtos antes de finalizar.");
                return;
            }
            if (txtNome.getText().isBlank()) {
                lblMensagemPedido.setText("Informe o nome do cliente.");
                return;
            }

            Pagamento pagamento = criarPagamento(cmbPagamento.getValue());
            if (pagamento == null) {
                lblMensagemPedido.setText("Escolha a forma de pagamento.");
                return;
            }

            Cliente cliente = new Cliente(txtNome.getText().trim(),
                    txtCpf.getText().trim(), txtEmail.getText().trim());

            // a regra de negócio fica no serviço; aqui só tratamos o resultado
            try {
                Pedido pedido = servicoPedido.finalizar(carrinho, cliente, pagamento);
                pedidos.add(pedido);

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Pedido finalizado");
                alerta.setHeaderText(null);
                alerta.setContentText(String.format("Pedido de %s finalizado com sucesso!%nPago com %s: R$ %.2f",
                        cliente.getNome(), pedido.getFormaPagamento(), pedido.getValorFinal()));
                alerta.showAndWait();

                // carrinho foi limpo pelo serviço: remonta a tela vazia
                raiz.setCenter(criarAreaCarrinho());
            } catch (PagamentoRecusadoException ex) {
                lblMensagemPedido.setText("Pagamento recusado: " + ex.getMessage());
            } catch (EstoqueInsuficienteException ex) {
                lblMensagemPedido.setText(ex.getMessage());
            }
        });

        HBox linhaPagamento = new HBox(10, cmbPagamento, btnFinalizar, lblMensagemPedido);
        linhaPagamento.setAlignment(Pos.CENTER_LEFT);

        VBox area = new VBox(10, subtitulo, lista, acoes, resumo,
                new Separator(), tituloFinalizar, linhaCliente, linhaPagamento);
        VBox.setVgrow(lista, Priority.ALWAYS);
        area.setPadding(new Insets(15));
        return area;
    }

    // transforma o texto escolhido na caixa em um objeto de pagamento;
    // devolve null se nada foi escolhido
    private Pagamento criarPagamento(String opcao) {
        if (opcao == null) {
            return null;
        }
        switch (opcao) {
            case "Pix":
                return new Pix();
            case "Cartão de crédito":
                return new CartaoCredito();
            case "Boleto":
                return new Boleto();
            default:
                return null;
        }
    }

    // ---------- Rodapé ----------
    private Node criarRodape() {
        Label total = new Label("Produtos no catálogo: " + catalogo.listar().size());

        HBox rodape = new HBox(total);
        rodape.setPadding(new Insets(10));
        return rodape;
    }
}