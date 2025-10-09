package main.java.xadrez.modelo;

import main.java.xadrez.Movimento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MotorXadrezAI {
    private final Dificuldade dificuldade;
    private final Random random = new Random();

    public MotorXadrezAI(Dificuldade dificuldade) {
        this.dificuldade = dificuldade;
    }

    public Movimento getJogada(Tabuleiro tabuleiro) {
        if (dificuldade == Dificuldade.FACIL) {
            return getJogadaAleatoria(tabuleiro);
        } else { // Dificuldade.MODERADO
            return getJogadaGuloso(tabuleiro);
        }
    }

    /**
     * Lógica da IA Nível Fácil: Escolhe um movimento legal aleatório.
     */
    private Movimento getJogadaAleatoria(Tabuleiro tabuleiro) {
        List<Movimento> todosMovimentosLegais = tabuleiro.gerarTodosMovimentosLegais(PecaCor.PRETA);

        if (todosMovimentosLegais.isEmpty()) {
            return null; // Xeque-mate ou empate
        }

        // Retorna um movimento aleatório da lista
        return todosMovimentosLegais.get(random.nextInt(todosMovimentosLegais.size()));
    }

    /**
     * Lógica da IA Nível Moderado: Escolhe o movimento que resulta na melhor pontuação imediata (guloso).
     * O principal critério é a captura de peças de maior valor.
     */
    private Movimento getJogadaGuloso(Tabuleiro tabuleiro) {
        List<Movimento> todosMovimentosLegais = tabuleiro.gerarTodosMovimentosLegais(PecaCor.PRETA);

        if (todosMovimentosLegais.isEmpty()) {
            return null; // Xeque-mate ou empate
        }

        List<Movimento> melhoresMovimentos = new ArrayList<>();
        int maiorPontuacao = Integer.MIN_VALUE;

        for (Movimento movimento : todosMovimentosLegais) {
            int pontuacao = avaliarMovimento(movimento);
            if (pontuacao > maiorPontuacao) {
                maiorPontuacao = pontuacao;
                melhoresMovimentos.clear();
                melhoresMovimentos.add(movimento);
            } else if (pontuacao == maiorPontuacao) {
                melhoresMovimentos.add(movimento);
            }
        }

        return melhoresMovimentos.get(random.nextInt(melhoresMovimentos.size()));
    }

    private int avaliarMovimento(Movimento movimento) {
        Casa destino = movimento.destino();
        if (destino.estaOcupada()) {
            Peca pecaCapturada = destino.getPeca();
            return getValorDaPeca(pecaCapturada);
        }
        return 0;
    }

    private int getValorDaPeca(Peca peca) {
        if (peca == null) return 0;
        String tipo = peca.getClass().getSimpleName();
        return switch (tipo) {
            case "Peao" -> 10;
            case "Cavalo", "Bispo" -> 30;
            case "Torre" -> 50;
            case "Dama" -> 90;
            case "Rei" -> 900;
            default -> 0;
        };
    }
}