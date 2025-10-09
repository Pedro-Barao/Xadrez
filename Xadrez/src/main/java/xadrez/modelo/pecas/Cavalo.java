package main.java.xadrez.modelo.pecas;

import main.java.xadrez.modelo.*;
import java.util.ArrayList;
import java.util.List;

public class Cavalo extends Peca {
    public Cavalo(PecaCor cor, String id) {
        super(cor, id);
    }

    @Override
    public List<Casa> getMovimentosPossiveis(Tabuleiro tabuleiro) {
        List<Casa> movimentos = new ArrayList<>();
        int[][] movimentosEmL = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};

        for (int[] m : movimentosEmL) {
            int novaLinha = casa.getLinha() + m[0];
            int novaColuna = casa.getColuna() + m[1];

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