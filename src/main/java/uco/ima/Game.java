package uco.ima;

public class Game {
    private int size;  // Déclare la variable 'size'
    private PlayerColor[][] grid;
    private PlayerColor currentPlayer, theWinner;
    private boolean over;
    private int capturesWhite, capturesBlack;
    private AbstractPlayer whitePlayer;
    private AbstractPlayer blackPlayer;
    private boolean egalite;

    // --------------------------------------
    // Constructeur principal (existant)
    // --------------------------------------
    public Game(int size, AbstractPlayer whitePlayer, AbstractPlayer blackPlayer) {
        this.size = size;  // Initialisation de la variable 'size'
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.currentPlayer = PlayerColor.WHITE;
        this.theWinner = null;
        this.over = false;
        this.capturesWhite = 0;
        this.capturesBlack = 0;
        this.grid = new PlayerColor[size][size];
    }

    // --------------------------------------
    // Nouveau : Constructeur de copie
    // --------------------------------------
    public Game(Game other) {
        // 1) Copier la taille
        this.size = other.size;

        // 2) Allouer et copier la grille
        this.grid = new PlayerColor[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.grid[i][j] = other.grid[i][j];
            }
        }

        // 3) Copier le joueur courant et l'état
        this.currentPlayer = other.currentPlayer;
        this.theWinner = other.theWinner;
        this.over = other.over;

        // 4) Copier le compteur de captures
        this.capturesWhite = other.capturesWhite;
        this.capturesBlack = other.capturesBlack;

        // 5) Copier les joueurs
        // Pour le Minimax, on peut laisser les mêmes références,
        // ou recréer d'autres IA/Humans. À toi de voir.
        this.whitePlayer = other.whitePlayer;
        this.blackPlayer = other.blackPlayer;
    }

    // --------------------------------------
    // Nouveau : Méthode clone()
    // --------------------------------------
    @Override
    public Game clone() {
        return new Game(this);
    }

    // --------------------------------------
    // Méthodes existantes
    // --------------------------------------
    public AbstractPlayer getCurrentPlayer() {
        return (currentPlayer == PlayerColor.WHITE) ? whitePlayer : blackPlayer;
    }

    public int getSize() {
        return size;
    }

    public PlayerColor getCurrentPlayerColor() {
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

        // Place la pierre
        grid[x][y] = currentPlayer;

        // Vérifie les captures
        checkAndCapture(x, y);

        // Vérifie si un joueur a gagné
        if (checkWin(x, y)) {
            over = true;
            theWinner = currentPlayer;
        }
        else if (capturesWhite >= 10 || capturesBlack >= 10) {
            // Victoire par captures
            over = true;
            theWinner = (capturesWhite >= 10) ? PlayerColor.WHITE : PlayerColor.BLACK;
        }
        else {
            // --- Nouveau : test d'égalité ---
            if (isBoardFull()) {
                over = true;
                egalite = true;       // On signale l'égalité
                theWinner = null; // Pas de gagnant
            }
        }

        // Change de joueur s'il n'y a pas de fin
        if (!over) {
            currentPlayer = currentPlayer.inverse();
        }
    }

    private void checkAndCapture(int x, int y) {
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        for (int[] dir : directions) {
            int dx = dir[0], dy = dir[1];

            // Vérifier la capture dans une direction
            if (canCapture(x, y, dx, dy)) {
                captureStones(x, y, dx, dy);
            }

            // Vérifier la capture dans la direction opposée
            if (canCapture(x, y, -dx, -dy)) {
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
                && grid[x3][y3] == currentPlayer;          // Pierre actuelle du joueur
    }

    private void captureStones(int x, int y, int dx, int dy) {
        int x1 = x + dx, y1 = y + dy;
        int x2 = x + 2 * dx, y2 = y + 2 * dy;

        if (isWithinBounds(x1, y1) && isWithinBounds(x2, y2)) {
            grid[x1][y1] = null;
            grid[x2][y2] = null;

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
            if (countConsecutive(x, y, dx, dy)
                    + countConsecutive(x, y, -dx, -dy) - 1 >= 5) {
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

    private boolean isBoardFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
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
    public boolean isEgalite() {
        return egalite;
    }
}
