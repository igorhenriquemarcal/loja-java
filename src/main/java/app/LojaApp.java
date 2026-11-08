package app;

import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Alimento;
import modelo.Carrinho;
import modelo.Catalogo;
import modelo.Eletronico;
import modelo.ItemCarrinho;
import modelo.Produto;
import modelo.Roupa;

public class LojaApp extends Application {

    private final Catalogo catalogo = new Catalogo();
    private final Carrinho carrinho = new Carrinho();

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

        Scene cena = new Scene(raiz, 900, 560);

        stage.setTitle("Loja Java");
        stage.setScene(cena);
        stage.show();
    }

    // dados temporários, só para a tela ter o que mostrar
    private void carregarDadosDeExemplo() {
        catalogo.adicionar(new Eletronico(1, "Fone", 200.0, 5, 12));
        catalogo.adicionar(new Roupa(2, "Camiseta", 80.0, 20, "M"));
        catalogo.adicionar(new Alimento(3, "Arroz 5kg", 30.0, 50, "31/12/2026"));

        // temporário: sai quando o botão "Adicionar ao carrinho" existir
        carrinho.adicionar(catalogo.buscar(1), 1);
        carrinho.adicionar(catalogo.buscar(2), 2);
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

        ListView<String> lista = new ListView<>();
        lista.setPlaceholder(new Label("Nenhum produto encontrado"));
        preencherLista(lista, catalogo.listar());

        // a cada tecla, refaz a lista só com os produtos que batem
        campoBusca.textProperty().addListener((obs, textoAntigo, textoNovo) ->
                preencherLista(lista, catalogo.buscarPorNome(textoNovo)));

        VBox area = new VBox(10, subtitulo, campoBusca, lista);
        VBox.setVgrow(lista, Priority.ALWAYS);   // a lista ocupa o espaço que sobrar
        area.setPadding(new Insets(15));
        return area;
    }

    private void preencherLista(ListView<String> lista, List<Produto> produtos) {
        lista.getItems().clear();
        for (Produto p : produtos) {
            lista.getItems().add(String.format("[%d] %s | R$ %.2f | estoque: %d | %s",
                    p.getCodigo(), p.getNome(), p.getPreco(), p.getEstoque(), p.descricaoExtra()));
        }
    }

    // ---------- Centro: carrinho ----------
    private Node criarAreaCarrinho() {
        Label subtitulo = new Label("Carrinho");
        subtitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<String> lista = new ListView<>();
        lista.setPlaceholder(new Label("Carrinho vazio"));
        for (ItemCarrinho item : carrinho.getItens()) {
            Produto p = item.getProduto();
            lista.getItems().add(String.format("%dx %s | R$ %.2f cada | subtotal: R$ %.2f",
                    item.getQuantidade(), p.getNome(), p.getPreco(), item.getSubtotal()));
        }

        Label lblSubtotal = new Label(String.format("Subtotal: R$ %.2f", carrinho.getTotal()));
        Label lblFrete = new Label(String.format("Frete: R$ %.2f", carrinho.getFrete()));
        Label lblTotal = new Label(String.format("Total: R$ %.2f",
                carrinho.getTotal() + carrinho.getFrete()));
        lblTotal.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        VBox resumo = new VBox(5, lblSubtotal, lblFrete, lblTotal);

        VBox area = new VBox(10, subtitulo, lista, resumo);
        VBox.setVgrow(lista, Priority.ALWAYS);
        area.setPadding(new Insets(15));
        return area;
    }

    // ---------- Rodapé ----------
    private Node criarRodape() {
        Label total = new Label("Produtos no catálogo: " + catalogo.listar().size());

        HBox rodape = new HBox(total);
        rodape.setPadding(new Insets(10));
        return rodape;
    }
}