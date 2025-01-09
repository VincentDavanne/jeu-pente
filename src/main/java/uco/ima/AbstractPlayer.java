package uco.ima;

public abstract class AbstractPlayer {
    protected PlayerColor playerColor;

    public AbstractPlayer(PlayerColor color) {
        this.playerColor = color;
    }

    public PlayerColor getColor() {
        return playerColor;
    }

    // Méthode abstraite que chaque type de joueur doit implémenter
    public abstract Position getMove(Game game);
}

