package main.java.xadrez.modelo;

import main.java.xadrez.Movimento;
import main.java.xadrez.modelo.pecas.*;
import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {
    private final Casa[][] casas = new Casa[8][8];
    private PecaCor turnoAtual;
    private EstadoJogo estadoJogo;

    public Tabuleiro() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                casas[i][j] = new Casa(i, j);
            }
        }
        iniciarPartida();
    }

    public void iniciarPartida() {
        casas[0][0].setPeca(new Torre(PecaCor.PRETA, "pretaTorre1"));
        casas[0][1].setPeca(new Cavalo(PecaCor.PRETA, "pretaCavalo1"));
        casas[0][2].setPeca(new Bispo(PecaCor.PRETA, "pretaBispo1"));
        casas[0][3].setPeca(new Dama(PecaCor.PRETA, "pretaDama"));
        casas[0][4].setPeca(new Rei(PecaCor.PRETA, "pretaRei"));
        casas[0][5].setPeca(new Bispo(PecaCor.PRETA, "pretaBispo2"));
        casas[0][6].setPeca(new Cavalo(PecaCor.PRETA, "pretaCavalo2"));
        casas[0][7].setPeca(new Torre(PecaCor.PRETA, "pretaTorre2"));
        for (int i = 0; i < 8; i++) { casas[1][i].setPeca(new Peao(PecaCor.PRETA, "pretaPeao" + (i + 1))); }

        casas[7][0].setPeca(new Torre(PecaCor.BRANCA, "brancaTorre1"));
        casas[7][1].setPeca(new Cavalo(PecaCor.BRANCA, "brancaCavalo1"));
        casas[7][2].setPeca(new Bispo(PecaCor.BRANCA, "brancaBispo1"));
        casas[7][3].setPeca(new Dama(PecaCor.BRANCA, "brancaDama"));
        casas[7][4].setPeca(new Rei(PecaCor.BRANCA, "brancaRei"));
        casas[7][5].setPeca(new Bispo(PecaCor.BRANCA, "brancaBispo2"));
        casas[7][6].setPeca(new Cavalo(PecaCor.BRANCA, "brancaCavalo2"));
        casas[7][7].setPeca(new Torre(PecaCor.BRANCA, "brancaTorre2"));
        for (int i = 0; i < 8; i++) { casas[6][i].setPeca(new Peao(PecaCor.BRANCA, "brancaPeao" + (i + 1))); }

        this.turnoAtual = PecaCor.BRANCA;
        this.estadoJogo = EstadoJogo.JOGANDO;
    }

    public void moverPeca(Casa origem, Casa destino) {
        if (!origem.estaOcupada()) return;

        Peca peca = origem.getPeca();
        if (destino.estaOcupada()) {
            System.out.println("Peça " + destino.getPeca().getId() + " capturada!");
        }

        origem.setPeca(null);
        destino.setPeca(peca);
        peca.setJaMoveu(true);

        trocarTurno();
        atualizarEstadoJogo();
    }

    public boolean movimentoDeixaReiEmXeque(Casa origem, Casa destino) {
        Peca pecaMovida = origem.getPeca();
        Peca pecaCapturada = destino.getPeca();
        PecaCor corAtual = pecaMovida.getCor();

        destino.setPeca(pecaMovida);
        origem.setPeca(null);

        boolean reiEmXeque = estaEmXeque(corAtual);

        origem.setPeca(pecaMovida);
        destino.setPeca(pecaCapturada);

        return reiEmXeque;
    }

    private void atualizarEstadoJogo() {
        if (gerarTodosMovimentosLegais(turnoAtual).isEmpty()) {
            if (estaEmXeque(turnoAtual)) {
                estadoJogo = EstadoJogo.XEQUE_MATE;
            } else {
                estadoJogo = EstadoJogo.EMPATE_AFOGAMENTO;
            }
        } else {
            estadoJogo = estaEmXeque(turnoAtual) ? EstadoJogo.XEQUE : EstadoJogo.JOGANDO;
        }
    }

    public List<Movimento> gerarTodosMovimentosLegais(PecaCor cor) {
        List<Movimento> movimentosLegais = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Casa casa = getCasa(i, j);
                if (casa != null && casa.estaOcupada() && casa.getPeca().getCor() == cor) {
                    for (Casa destino : casa.getPeca().getMovimentosValidos(this)) {
                        movimentosLegais.add(new Movimento(casa, destino));
                    }
                }
            }
        }
        return movimentosLegais;
    }

    public boolean estaEmXeque(PecaCor corDoRei) {
        Casa casaDoRei = encontrarRei(corDoRei);
        if (casaDoRei == null) return false;

        PecaCor corOponente = (corDoRei == PecaCor.BRANCA) ? PecaCor.PRETA : PecaCor.BRANCA;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Casa casaAtual = getCasa(i, j);
                if (casaAtual != null && casaAtual.estaOcupada() && casaAtual.getPeca().getCor() == corOponente) {
                    for (Casa movimentoPossivel : casaAtual.getPeca().getMovimentosPossiveis(this)) {
                        if (movimentoPossivel == casaDoRei) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private Casa encontrarRei(PecaCor cor) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Casa casa = getCasa(i, j);
                if (casa != null && casa.estaOcupada() && casa.getPeca().getCor() == cor && casa.getPeca() instanceof Rei) {
                    return casa;
                }
            }
        }
        return null;
    }

    public boolean coordenadaValida(int linha, int coluna) {
        return linha >= 0 && linha < 8 && coluna >= 0 && coluna < 8;
    }

    private void trocarTurno() {
        turnoAtual = (turnoAtual == PecaCor.BRANCA) ? PecaCor.PRETA : PecaCor.BRANCA;
    }

    public PecaCor getTurnoAtual() { return turnoAtual; }
    public EstadoJogo getEstadoJogo() { return estadoJogo; }

    /**
     * MÉTODO CORRIGIDO: Adiciona uma verificação de segurança para evitar
     * que coordenadas inválidas quebrem o programa.
     */
    public Casa getCasa(int linha, int coluna) {
        if (!coordenadaValida(linha, coluna)) {
            return null;
        }
        return casas[linha][coluna];
    }

    public PecaCor getVencedor() {
        if(estadoJogo == EstadoJogo.XEQUE_MATE) {
            return (turnoAtual == PecaCor.BRANCA) ? PecaCor.PRETA : PecaCor.BRANCA;
        }
        return null;
    }
}