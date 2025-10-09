package main.java.selecao;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.xadrez.XadrezController;
import main.java.xadrez.modelo.ModoDeJogo;
import java.io.IOException;
import java.util.Objects;

public class TelaSelecaoController {

    private void carregarJogo(ActionEvent event, ModoDeJogo modo) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/xadrez/Tabuleiro.fxml")));
        Parent tabuleiroRoot = loader.load();

        XadrezController xadrezController = loader.getController();
        xadrezController.iniciarJogo(modo);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene sceneDoTabuleiro = new Scene(tabuleiroRoot);

        sceneDoTabuleiro.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/xadrez/estilo.css")).toExternalForm());

        stage.setScene(sceneDoTabuleiro);
        stage.setTitle("Xadrez");
        stage.sizeToScene();
        stage.setResizable(true);
        stage.centerOnScreen();
    }

    @FXML
    void iniciarVsHumano(ActionEvent event) throws IOException {
        carregarJogo(event, ModoDeJogo.HUMANO_VS_HUMANO);
    }

    @FXML
    void iniciarVsIA(ActionEvent event) {
        try {
            System.out.println("Modo Humano vs IA selecionado! Carregando...");
            carregarJogo(event, ModoDeJogo.HUMANO_VS_IA);
        } catch (IOException e) {
            System.err.println("FALHA CRÍTICA AO CARREGAR O JOGO NO MODO VS IA!");
            e.printStackTrace();
        }
    }}