package main.java.xadrez.modelo.pecas;

import main.java.xadrez.modelo.*;
import java.util.ArrayList;
import java.util.List;

public class Torre extends Peca {
    public Torre(PecaCor cor, String id) {
        super(cor, id);
    }

    @Override
    public List<Casa> getMovimentosPossiveis(Tabuleiro tabuleiro) {
        List<Casa> movimentos = new ArrayList<>();
        int[][] direcoes = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}}; // Direita, Esquerda, Baixo, Cima

        for (int[] d : direcoes) {
            int proximaLinha = casa.getLinha() + d[0];
            int proximaColuna = casa.getColuna() + d[1];

            while (tabuleiro.coordenadaValida(proximaLinha, proximaColuna)) {
                Casa proximaCasa = tabuleiro.getCasa(proximaLinha, proximaColuna);
                if (proximaCasa.estaOcupada()) {
                    // Se a peça for inimiga, é um movimento válido (captura), mas bloqueia o caminho
                    if (proximaCasa.getPeca().getCor() != this.cor) {
                        movimentos.add(proximaCasa);
                    }
                    break; // Bloqueado por peça amiga ou inimiga
                }
                // Se a casa estiver vazia, é um movimento válido
                movimentos.add(proximaCasa);

                // Continua na mesma direção
                proximaLinha += d[0];
                proximaColuna += d[1];
            }
        }
        return movimentos;
    }
}