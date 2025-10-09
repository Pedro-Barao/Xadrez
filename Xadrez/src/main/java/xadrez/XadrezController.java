package main.java.xadrez;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import main.java.xadrez.modelo.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XadrezController {

    private Tabuleiro tabuleiro;
    private MotorXadrezAI ai;
    private ModoDeJogo modoDeJogo;
    private int startCol, startRow;
    private Map<Peca, ImageView> pecaParaImageViewMap = new HashMap<>();
    private Map<String, ImageView> idParaViewMap = new HashMap<>();

    @FXML private GridPane boardGridPane;
    @FXML private Label statusLabel;
    @FXML private ImageView brancaTorre1, brancaTorre2, brancaCavalo1, brancaCavalo2, brancaBispo1, brancaBispo2, brancaDama, brancaRei, brancaPeao1, brancaPeao2, brancaPeao3, brancaPeao4, brancaPeao5, brancaPeao6, brancaPeao7, brancaPeao8;
    @FXML private ImageView pretaTorre1, pretaTorre2, pretaCavalo1, pretaCavalo2, pretaBispo1, pretaBispo2, pretaDama, pretaRei, pretaPeao1, pretaPeao2, pretaPeao3, pretaPeao4, pretaPeao5, pretaPeao6, pretaPeao7, pretaPeao8;

    public void iniciarJogo(ModoDeJogo modo) {
        this.modoDeJogo = modo;
        this.tabuleiro = new Tabuleiro();

        if (this.modoDeJogo == ModoDeJogo.HUMANO_VS_IA) {
            this.ai = new MotorXadrezAI(Dificuldade.FACIL);
        }

        mapearPecas();
        desenharTabuleiro();
        atualizarStatus();
        setupDropTarget();
    }

    private void mapearPecas() {
        pecaParaImageViewMap.clear();
        mapearIdParaViews();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Peca pecaLogica = tabuleiro.getCasa(i, j).getPeca();
                if (pecaLogica != null) {
                    ImageView imageView = idParaViewMap.get(pecaLogica.getId());
                    pecaParaImageViewMap.put(pecaLogica, imageView);
                    makeDraggable(imageView);
                }
            }
        }
    }

    private void desenharTabuleiro() {
        limparDestaques();
        for (ImageView view : idParaViewMap.values()) {
            view.setVisible(false);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Peca pecaLogica = tabuleiro.getCasa(i, j).getPeca();
                if (pecaLogica != null) {
                    ImageView imageViewPeca = pecaParaImageViewMap.get(pecaLogica);
                    if (imageViewPeca != null) {
                        GridPane.setRowIndex(imageViewPeca, i);
                        GridPane.setColumnIndex(imageViewPeca, j);
                        imageViewPeca.setVisible(true);
                    }
                }
            }
        }
    }

    private void makeDraggable(ImageView pecaView) {
        pecaView.setOnDragDetected(event -> {
            Integer col = GridPane.getColumnIndex(pecaView);
            Integer row = GridPane.getRowIndex(pecaView);
            if (col == null || row == null || tabuleiro.getEstadoJogo() != EstadoJogo.JOGANDO) return;

            Peca pecaLogica = tabuleiro.getCasa(row, col).getPeca();

            if (pecaLogica != null && pecaLogica.getCor() == tabuleiro.getTurnoAtual()) {
                startCol = col;
                startRow = row;

                Dragboard db = pecaView.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(pecaView.getId());
                db.setContent(content);

                WritableImage dragImage = new WritableImage((int) pecaView.getFitWidth(), (int) pecaView.getFitHeight());
                SnapshotParameters params = new SnapshotParameters();
                params.setFill(Color.TRANSPARENT);
                pecaView.snapshot(params, dragImage);
                db.setDragView(dragImage, dragImage.getWidth() / 2, dragImage.getHeight() / 2);

                pecaView.setVisible(false);
                mostrarMovimentosValidos(pecaLogica);
                event.consume();
            }
        });

        pecaView.setOnDragDone(event -> {
            pecaView.setVisible(true);
            limparDestaques();
            event.consume();
        });
    }

    private void setupDropTarget() {
        boardGridPane.setOnDragOver(event -> {
            if (event.getGestureSource() != boardGridPane && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        boardGridPane.setOnDragDropped(event -> {
            boolean success = false;
            int newCol = (int) (event.getX() / (boardGridPane.getWidth() / 8));
            int newRow = (int) (event.getY() / (boardGridPane.getHeight() / 8));

            Casa origem = tabuleiro.getCasa(startRow, startCol);
            Casa destino = tabuleiro.getCasa(newRow, newCol);
            Peca pecaLogica = origem.getPeca();

            if (pecaLogica != null && pecaLogica.getMovimentosValidos(tabuleiro).contains(destino)) {
                tabuleiro.moverPeca(origem, destino);
                desenharTabuleiro();
                atualizarStatus();
                success = true;

                trocarTurno();
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void trocarTurno() {
        if (tabuleiro.getEstadoJogo() != EstadoJogo.JOGANDO) return;

        if (modoDeJogo == ModoDeJogo.HUMANO_VS_IA && tabuleiro.getTurnoAtual() == PecaCor.PRETA) {

            System.out.println("Turno da IA. Pensando...");

            Movimento jogadaIA = ai.getJogada(tabuleiro);

            if (jogadaIA != null) {
                tabuleiro.moverPeca(jogadaIA.origem(), jogadaIA.destino());
                desenharTabuleiro(); // Atualiza a interface
                atualizarStatus();
            } else {
                atualizarStatus();
            }
        }
    }

    private void atualizarStatus() {
        String status = "Turno: " + tabuleiro.getTurnoAtual();
        if (tabuleiro.getEstadoJogo() == EstadoJogo.XEQUE) {
            status += " - XEQUE!";
        } else if (tabuleiro.getEstadoJogo() == EstadoJogo.XEQUE_MATE) {
            status = "XEQUE-MATE! Vencedor: " + tabuleiro.getVencedor();
        } else if (tabuleiro.getEstadoJogo() == EstadoJogo.EMPATE_AFOGAMENTO) {
            status = "EMPATE POR AFOGAMENTO!";
        }
        statusLabel.setText(status);
    }

    private void mostrarMovimentosValidos(Peca peca) {
        List<Casa> movimentos = peca.getMovimentosValidos(tabuleiro);
        for (Casa casa : movimentos) {
            for (Node node : boardGridPane.getChildren()) {
                if (node instanceof Pane) {
                    Integer col = GridPane.getColumnIndex(node);
                    Integer row = GridPane.getRowIndex(node);

                    int c = (col == null) ? 0 : col;
                    int r = (row == null) ? 0 : row;

                    if(r == casa.getLinha() && c == casa.getColuna()){
                        node.getStyleClass().add("valid-move-cell");
                    }
                }
            }
        }
    }

    private void limparDestaques() {
        for (Node node : boardGridPane.getChildren()) {
            node.getStyleClass().remove("valid-move-cell");
        }
    }

    private void mapearIdParaViews() {
        idParaViewMap.put("brancaTorre1", brancaTorre1); idParaViewMap.put("brancaTorre2", brancaTorre2);
        idParaViewMap.put("brancaCavalo1", brancaCavalo1); idParaViewMap.put("brancaCavalo2", brancaCavalo2);
        idParaViewMap.put("brancaBispo1", brancaBispo1); idParaViewMap.put("brancaBispo2", brancaBispo2);
        idParaViewMap.put("brancaDama", brancaDama); idParaViewMap.put("brancaRei", brancaRei);
        idParaViewMap.put("brancaPeao1", brancaPeao1); idParaViewMap.put("brancaPeao2", brancaPeao2);
        idParaViewMap.put("brancaPeao3", brancaPeao3); idParaViewMap.put("brancaPeao4", brancaPeao4);
        idParaViewMap.put("brancaPeao5", brancaPeao5); idParaViewMap.put("brancaPeao6", brancaPeao6);
        idParaViewMap.put("brancaPeao7", brancaPeao7); idParaViewMap.put("brancaPeao8", brancaPeao8);
        idParaViewMap.put("pretaTorre1", pretaTorre1); idParaViewMap.put("pretaTorre2", pretaTorre2);
        idParaViewMap.put("pretaCavalo1", pretaCavalo1); idParaViewMap.put("pretaCavalo2", pretaCavalo2);
        idParaViewMap.put("pretaBispo1", pretaBispo1); idParaViewMap.put("pretaBispo2", pretaBispo2);
        idParaViewMap.put("pretaDama", pretaDama); idParaViewMap.put("pretaRei", pretaRei);
        idParaViewMap.put("pretaPeao1", pretaPeao1); idParaViewMap.put("pretaPeao2", pretaPeao2);
        idParaViewMap.put("pretaPeao3", pretaPeao3); idParaViewMap.put("pretaPeao4", pretaPeao4);
        idParaViewMap.put("pretaPeao5", pretaPeao5); idParaViewMap.put("pretaPeao6", pretaPeao6);
        idParaViewMap.put("pretaPeao7", pretaPeao7); idParaViewMap.put("pretaPeao8", pretaPeao8);
    }
}