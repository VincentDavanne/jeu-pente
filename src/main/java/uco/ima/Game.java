package uco.ima;

public class Game {
    // Attributs (inchangés)
    private int size;
    private PlayerColor[][] grid;
    private PlayerColor currentPlayer, theWinner;
    private boolean over;
    private int capturesWhite, capturesBlack;

    public Game(int size) {
        this.size = size;
        grid = new PlayerColor[size][size];
        currentPlayer = PlayerColor.BLACK; // Commence avec BLACK
        theWinner = null;
        over = false;
        capturesWhite = 0;
        capturesBlack = 0;
    }

    public int getSize() {
        return size;
    }

    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isMoveValid(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        return x >= 0 && x < size && y >= 0 && y < size && grid[x][y] == null;
    }

    public void makeMove(Position pos) {
        int x = pos.getX();
        int y = pos.getY();

        if (!isMoveValid(pos)) {
            throw new IllegalArgumentException("Coup invalide !");
        }

        // Place la pierre sur la grille
        grid[x][y] = currentPlayer;

        // Vérifie les captures
        checkAndCapture(x, y);

        // Vérifie si un joueur a gagné
        if (checkWin(x, y)) {
            over = true;
            theWinner = currentPlayer;
        } else if (capturesWhite >= 10 || capturesBlack >= 10) { // Victoire par captures
            over = true;
            theWinner = (capturesWhite >= 10) ? PlayerColor.WHITE : PlayerColor.BLACK;
        }

        // Change de joueur
        currentPlayer = currentPlayer.inverse();
    }

    private void checkAndCapture(int x, int y) {
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        for (int[] dir : directions) {
            int dx = dir[0], dy = dir[1];

            // Vérifier la capture dans une direction
            if (canCapture(x, y, dx, dy)) {
                System.out.println("Capture détectée à partir de (" + x + "," + y + ") dans la direction (" + dx + "," + dy + ")");
                captureStones(x, y, dx, dy);
            }

            // Vérifier la capture dans la direction opposée
            if (canCapture(x, y, -dx, -dy)) {
                System.out.println("Capture détectée à partir de (" + x + "," + y + ") dans la direction opposée (" + -dx + "," + -dy + ")");
                captureStones(x, y, -dx, -dy);
            }
        }
    }

    private boolean canCapture(int x, int y, int dx, int dy) {
        int x1 = x + dx, y1 = y + dy;
        int x2 = x + 2 * dx, y2 = y + 2 * dy;
        int x3 = x + 3 * dx, y3 = y + 3 * dy;

        // Vérifie si les coordonnées sont dans les limites de la grille
        if (!isWithinBounds(x1, y1) || !isWithinBounds(x2, y2) || !isWithinBounds(x3, y3)) {
            return false;
        }

        // Vérifie si les pierres capturées et de bordure sont correctes
        return grid[x1][y1] == currentPlayer.inverse() // Première pierre adverse
                && grid[x2][y2] == currentPlayer.inverse() // Deuxième pierre adverse
                && grid[x3][y3] == currentPlayer;         // Pierre actuelle du joueur
    }

    private void captureStones(int x, int y, int dx, int dy) {
        int x1 = x + dx, y1 = y + dy;
        int x2 = x + 2 * dx, y2 = y + 2 * dy;

        // Vérifie si les pierres à capturer sont bien dans les limites
        if (isWithinBounds(x1, y1) && isWithinBounds(x2, y2)) {
            System.out.println("Pierres capturées : (" + x1 + "," + y1 + ") et (" + x2 + "," + y2 + ")");
            grid[x1][y1] = null;
            grid[x2][y2] = null;

            // Met à jour le compteur de captures
            if (currentPlayer == PlayerColor.BLACK) {
                capturesBlack += 2;
            } else {
                capturesWhite += 2;
            }
        }
    }



    private boolean checkWin(int x, int y) {
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

        for (int[] dir : directions) {
            int dx = dir[0], dy = dir[1];
            if (countConsecutive(x, y, dx, dy) + countConsecutive(x, y, -dx, -dy) - 1 >= 5) {
                return true;
            }
        }
        return false;
    }

    private int countConsecutive(int x, int y, int dx, int dy) {
        int count = 0;
        PlayerColor color = grid[x][y];

        while (isWithinBounds(x, y) && grid[x][y] == color) {
            count++;
            x += dx;
            y += dy;
        }
        return count;
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public PlayerColor getPion(int x, int y) {
        return grid[x][y];
    }

    public boolean isOver() {
        return over;
    }

    public PlayerColor getWinner() {
        return theWinner;
    }

    public int getCaptures(PlayerColor player) {
        return (player == PlayerColor.WHITE) ? capturesWhite : capturesBlack;
    }
}
