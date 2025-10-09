package main.java.xadrez.modelo.pecas;

import main.java.xadrez.modelo.*;
import java.util.ArrayList;
import java.util.List;

public class Dama extends Peca {
    public Dama(PecaCor cor, String id) {
        super(cor, id);
    }

    @Override
    public List<Casa> getMovimentosPossiveis(Tabuleiro tabuleiro) {
        List<Casa> movimentos = new ArrayList<>();
        // Lógica da Dama = 8 direções da Torre e do Bispo
        int[][] direcoes = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] d : direcoes) {
            int proximaLinha = casa.getLinha() + d[0];
            int proximaColuna = casa.getColuna() + d[1];

            while (tabuleiro.coordenadaValida(proximaLinha, proximaColuna)) {
                Casa proximaCasa = tabuleiro.getCasa(proximaLinha, proximaColuna);
                if (proximaCasa.estaOcupada()) {
                    if (proximaCasa.getPeca().getCor() != this.cor) {
                        movimentos.add(proximaCasa); // Captura
                    }
                    break; // Bloqueado
                }
                movimentos.add(proximaCasa); // Movimento
                proximaLinha += d[0];
                proximaColuna += d[1];
            }
        }
        return movimentos;
    }
}