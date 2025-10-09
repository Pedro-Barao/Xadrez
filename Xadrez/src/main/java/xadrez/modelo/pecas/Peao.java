package main.java.xadrez.modelo.pecas;

import main.java.xadrez.modelo.*;
import java.util.ArrayList;
import java.util.List;

public class Peao extends Peca {
    public Peao(PecaCor cor, String id) {
        super(cor, id);
    }

    @Override
    public List<Casa> getMovimentosPossiveis(Tabuleiro tabuleiro) {
        List<Casa> movimentos = new ArrayList<>();
        int direcao = (getCor() == PecaCor.BRANCA) ? -1 : 1;
        int linhaAtual = casa.getLinha();
        int colunaAtual = casa.getColuna();

        if (tabuleiro.coordenadaValida(linhaAtual + direcao, colunaAtual)) {
            Casa frente1 = tabuleiro.getCasa(linhaAtual + direcao, colunaAtual);
            if (!frente1.estaOcupada()) {
                movimentos.add(frente1);
                if (!isJaMoveu()) {
                    if (tabuleiro.coordenadaValida(linhaAtual + 2 * direcao, colunaAtual)) {
                        Casa frente2 = tabuleiro.getCasa(linhaAtual + 2 * direcao, colunaAtual);
                        if (!frente2.estaOcupada()) {
                            movimentos.add(frente2);
                        }
                    }
                }
            }
        }

        int[] colunasCaptura = {colunaAtual - 1, colunaAtual + 1};
        for (int colunaDestino : colunasCaptura) {
            if (tabuleiro.coordenadaValida(linhaAtual + direcao, colunaDestino)) {
                Casa destino = tabuleiro.getCasa(linhaAtual + direcao, colunaDestino);
                if (destino.estaOcupada() && destino.getPeca().getCor() != this.cor) {
                    movimentos.add(destino);
                }
            }
        }

        return movimentos;
    }}