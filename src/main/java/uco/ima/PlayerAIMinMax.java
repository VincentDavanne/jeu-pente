package uco.ima;

import java.util.ArrayList;
import java.util.List;

/**
 * IA basée sur l'algorithme Minimax avec élagage alpha-bêta.
 */
public class PlayerAIMinMax extends AbstractPlayer {

    private static final int MAX_DEPTH = 5;  // Ajustez la profondeur à votre convenance

    // --- Nouveau : compteur pour suivre le nombre de nœuds explorés ---
    private static long nodesVisited = 0;

    public PlayerAIMinMax(PlayerColor color) {
        super(color);
    }

    @Override
    public Position getMove(Game game) {
        // Remettre le compteur à zéro au début d'un nouveau coup
        nodesVisited = 0;

        List<Position> possibleMoves = getAllValidMoves(game);
        if (possibleMoves.isEmpty()) {
            return null;
        }

        Position bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        // "isMaximizing" : est-ce le tour de l’IA elle-même ?
        boolean isMaximizing = (game.getCurrentPlayerColor() == this.getColor());

        // Initialisation alpha/beta
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (Position move : possibleMoves) {
            Game clonedGame = cloneGame(game);
            clonedGame.makeMove(move);

            int moveValue = alphaBetaMinimax(clonedGame, MAX_DEPTH - 1, alpha, beta, !isMaximizing);

            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
            alpha = Math.max(alpha, bestValue);
        }

        // Affiche ou log le nombre de nœuds visités
        System.out.println("Nombre de nœuds explorés (alpha-bêta) : " + nodesVisited);

        return bestMove;
    }

    /**
     * Minimax avec élagage alpha-bêta.
     */
    private int alphaBetaMinimax(Game game, int depth, int alpha, int beta, boolean isMaximizing) {
        // Incrémenter le compteur de nœuds
        nodesVisited++;

        // Condition d'arrêt
        if (depth == 0 || game.isOver()) {
            return evaluateBoard(game);
        }

        List<Position> possibleMoves = getAllValidMoves(game);
        if (possibleMoves.isEmpty()) {
            return evaluateBoard(game);
        }

        if (isMaximizing) {
            int value = Integer.MIN_VALUE;
            for (Position move : possibleMoves) {
                Game clonedGame = cloneGame(game);
                clonedGame.makeMove(move);

                int moveValue = alphaBetaMinimax(clonedGame, depth - 1, alpha, beta, false);
                value = Math.max(value, moveValue);

                alpha = Math.max(alpha, value);
                if (alpha >= beta) {
                    break;  // coupure
                }
            }
            return value;
        } else {
            int value = Integer.MAX_VALUE;
            for (Position move : possibleMoves) {
                Game clonedGame = cloneGame(game);
                clonedGame.makeMove(move);

                int moveValue = alphaBetaMinimax(clonedGame, depth - 1, alpha, beta, true);
                value = Math.min(value, moveValue);

                beta = Math.min(beta, value);
                if (beta <= alpha) {
                    break;  // coupure
                }
            }
            return value;
        }
    }

    /**
     * Évalue rapidement l'état du plateau.
     */
    private int evaluateBoard(Game game) {
        if (game.isOver()) {
            if (game.getWinner() == this.getColor()) {
                return 100000;  // Gagnant
            } else {
                return -100000; // Perdant
            }
        }
        int myCaptures = game.getCaptures(this.getColor());
        int oppCaptures = game.getCaptures(this.getColor().inverse());
        return myCaptures - oppCaptures;
    }

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

    private Game cloneGame(Game original) {
        return new Game(original);
    }
}
