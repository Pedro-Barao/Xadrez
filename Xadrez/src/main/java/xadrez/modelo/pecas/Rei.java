package main.java.xadrez.modelo.pecas;

import main.java.xadrez.modelo.*;
import java.util.ArrayList;
import java.util.List;

public class Rei extends Peca {
    public Rei(PecaCor cor, String id) {
        super(cor, id);
    }

    @Override
    public List<Casa> getMovimentosPossiveis(Tabuleiro tabuleiro) {
        List<Casa> movimentos = new ArrayList<>();
        int[][] vizinhos = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

        for (int[] v : vizinhos) {
            int novaLinha = casa.getLinha() + v[0];
            int novaColuna = casa.getColuna() + v[1];

            if (tabuleiro.coordenadaValida(novaLinha, novaColuna)) {
                Casa destino = tabuleiro.getCasa(novaLinha, novaColuna);
                if (!destino.estaOcupada() || destino.getPeca().getCor() != this.cor) {
                    movimentos.add(destino);
                }
            }
        }
        return movimentos;
    }
}