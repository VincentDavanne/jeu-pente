package uco.ima;

public enum PlayerColor {
    WHITE, BLACK;

    public PlayerColor inverse() {
        if (this == WHITE)
            return BLACK;
        else
            return WHITE;
    }
}
