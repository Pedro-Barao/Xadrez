package main.java.xadrez.modelo;

public class Casa {
    private final int linha;
    private final int coluna;
    private Peca peca;

    public Casa(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public Peca getPeca() {
        return peca;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
        // A CORREÇÃO ESTÁ AQUI:
        if (peca != null) {
            peca.setCasa(this);
        }
    }

    public boolean estaOcupada() {
        return peca != null;
    }
}