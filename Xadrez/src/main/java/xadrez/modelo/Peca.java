package main.java.xadrez.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Peca {
    protected final PecaCor cor;
    protected Casa casa;
    protected boolean jaMoveu;
    private final String id;

    public Peca(PecaCor cor, String id) {
        this.cor = cor;
        this.id = id;
        this.jaMoveu = false;
    }

    public List<Casa> getMovimentosValidos(Tabuleiro tabuleiro) {
        return getMovimentosPossiveis(tabuleiro).stream()
                .filter(destino -> !tabuleiro.movimentoDeixaReiEmXeque(this.casa, destino))
                .collect(Collectors.toList());
    }

    public abstract List<Casa> getMovimentosPossiveis(Tabuleiro tabuleiro);

    protected void adicionarMovimento(List<Casa> movimentos, Casa casa) {
        if (casa != null) {
            if (!casa.estaOcupada()) {
                movimentos.add(casa);
            } else if (casa.getPeca().getCor() != this.cor) {
                movimentos.add(casa); // Captura
            }
        }
    }

    public void setJaMoveu(boolean jaMoveu) { this.jaMoveu = jaMoveu; }
    public PecaCor getCor() { return cor; }
    public Casa getCasa() { return casa; }
    public void setCasa(Casa casa) { this.casa = casa; }
    public boolean isJaMoveu() { return jaMoveu; }
    public String getId() { return id; }
}