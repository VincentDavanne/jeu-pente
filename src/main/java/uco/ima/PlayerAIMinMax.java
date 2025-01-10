package uco.ima;

import java.util.ArrayList;
import java.util.List;

/**
 * IA basée sur l'algorithme Minimax.
 */
public class PlayerAIMinMax extends AbstractPlayer {

    // Profondeur maximale de l'arbre
    private static final int MAX_DEPTH = 5;  // Ajuste selon tes besoins

    public PlayerAIMinMax(PlayerColor color) {
        super(color);
    }

    @Override
    public Position getMove(Game game) {
        // Récupère tous les coups possibles
        List<Position> possibleMoves = getAllValidMoves(game);

        // Si pas de coup possible
        if (possibleMoves.isEmpty()) {
            return null;
        }

        // On choisit le meilleur coup selon Minimax
        Position bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        // "isMaximizing" = est-ce que c'est le tour de NOTRE IA ?
        boolean isMaximizing = (game.getCurrentPlayerColor() == this.getColor());

        for (Position move : possibleMoves) {
            // 1. Cloner l'état du jeu
            Game clonedGame = cloneGame(game);

            // 2. Jouer ce coup dans le clone
            clonedGame.makeMove(move);

            // 3. Évaluer la position à profondeur (MAX_DEPTH - 1)
            int moveValue = minimax(clonedGame, MAX_DEPTH - 1, !isMaximizing);

            // Au niveau supérieur, on choisit le coup qui MAXIMISE le score
            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * Algorithme Minimax simple (pas d'alpha-bêta).
     */
    private int minimax(Game game, int depth, boolean isMaximizing) {
        // Condition d'arrêt
        if (depth == 0 || game.isOver()) {
            return evaluateBoard(game);
        }

        List<Position> possibleMoves = getAllValidMoves(game);
        if (possibleMoves.isEmpty()) {
            return evaluateBoard(game);
        }

        if (isMaximizing) {
            int bestVal = Integer.MIN_VALUE;
            for (Position move : possibleMoves) {
                // Clone + makeMove
                Game clonedGame = cloneGame(game);
                clonedGame.makeMove(move);

                int value = minimax(clonedGame, depth - 1, false);
                bestVal = Math.max(bestVal, value);
            }
            return bestVal;
        } else {
            int bestVal = Integer.MAX_VALUE;
            for (Position move : possibleMoves) {
                Game clonedGame = cloneGame(game);
                clonedGame.makeMove(move);

                int value = minimax(clonedGame, depth - 1, true);
                bestVal = Math.min(bestVal, value);
            }
            return bestVal;
        }
    }

    /**
     * Évalue rapidement l'état du plateau.
     */
    private int evaluateBoard(Game game) {
        // Si la partie est terminée, on regarde le gagnant
        if (game.isOver()) {
            if (game.getWinner() == this.getColor()) {
                // IA gagne => très grand score
                return 100000;
            } else {
                // Adversaire gagne => très petit score
                return -100000;
            }
        }

        // Sinon, heuristique simple : différence de captures
        int myCaptures = game.getCaptures(this.getColor());
        int oppCaptures = game.getCaptures(this.getColor().inverse());
        return (myCaptures - oppCaptures);
    }

    /**
     * Retourne la liste de tous les coups valides.
     */
    private List<Position> getAllValidMoves(Game game) {
        List<Position> moves = new ArrayList<>();
        int size = game.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Position pos = new Position(i, j);
                if (game.isMoveValid(pos)) {
                    moves.add(pos);
                }
            }
        }
        return moves;
    }

    /**
     * Utilise le constructeur de copie
     */
    private Game cloneGame(Game original) {
        // Avec le nouveau constructeur de copie ou la méthode clone()
        return new Game(original);
        // ou : return original.clone();
    }
}
