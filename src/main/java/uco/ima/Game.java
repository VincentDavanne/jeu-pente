package uco.ima;

public class Game {
    private int size;
    private PlayerColor[][] pion;
    private PlayerColor currentPlayer, theWinner;
    private boolean over;
    private int direction;		// variation de x des déplacements de currentPlayer

    public Game(int size) {
        this.size = size;
        // Créer et initialiser le tableau pion
        pion = new PlayerColor [size][size];
        for (int j = 0; j < size; j++) {
            pion[0][j] = PlayerColor.BLACK;
            pion[size-1][j] = PlayerColor.WHITE;
        }
        currentPlayer = PlayerColor.WHITE;
        direction = -1;
        theWinner = null;
        over = false;
    }
    public boolean moveValid(Move move) {
        // Vérifier que la case de départ contient currentPlayer.
        // Si currentPlayer est WHITE :
        // - la case d'arrivée est au dessus de la case de départ et est vide
        // - ou la case d'arrivée est au dessus en diagonale de la case de départ
        //   et contient la couleur adverse.
        // Si currentPlayer est BLACK : similaire dans la direction inverse
        int x1 = move.getStart().getX(), y1 = move.getStart().getY();
        int x2 = move.getEnd().getX(), y2 = move.getEnd().getY();
        if (pion[x1][y1] == currentPlayer) {
            if (x2 == x1 + direction && y2 == y1 && pion[x2][y2] == null)
                return true;
            else if (x2 == x1 + direction && Math.abs(y2 - y1) == 1
                    && pion[x2][y2] == currentPlayer.inverse())
                return true;
        }
        return false;
    }
    public void makeMove(Move move) {
        // Mettre la couleur de la case de départ à la case d'arrivée
        // et vider la case de départ
        int x1 = move.getStart().getX(), y1 = move.getStart().getY();
        int x2 = move.getEnd().getX(), y2 = move.getEnd().getY();
        pion[x2][y2] = pion[x1][y1];
        pion[x1][y1] = null;
        // Vérifier si le match a fini : currentPlayer est arrivé
        // x2 = 0 pour currentPlayer WHITE ou x2 = size-1 pour currentPlayer BLACK
        if(x2 == 0 || x2 == size-1) {
            over = true;
            theWinner = currentPlayer;
        }
        currentPlayer = currentPlayer.inverse();
        direction = -direction;
        // Vérifier si le match a fini : currentPlayer ne peut plus bouger
        if (!canMove()) {
            over = true;
            theWinner = currentPlayer.inverse();
        }
    }
    public boolean canMove() {
        // Vérifie que le Player peut se déplacer
        for(int i = 0; i<size;i++) {
            for(int j = 0; j<size;j++) {
                if(pion[i][j] == currentPlayer) {
                    // Créer 3 déplacements à partir de cette positio
                    Position s = new Position(i,j),
                            e1 = new Position(i+direction,j),
                            e2 = new Position(i+direction,Math.max(0,j-1)),
                            e3 = new Position(i+direction,Math.min(j+1,size-1));
                    Move m1 = new Move(s,e1),
                            m2 = new Move(s,e2),
                            m3 = new Move(s,e3);
                    if(moveValid(m1)|| moveValid(m2)||moveValid(m3)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public PlayerColor getPion(int i, int j) {
        return pion[i][j];
    }
    public boolean isOver() {
        return over;
    }
    public PlayerColor getWinner() {
        return theWinner;
    }

}
