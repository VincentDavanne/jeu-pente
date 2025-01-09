package uco.ima;

public class HumanPlayer extends AbstractPlayer {

    public HumanPlayer(PlayerColor color) {
        super(color);
    }

    @Override
    public Position getMove(Game game) {
        // Les joueurs humains utilisent l'interface graphique pour jouer
        return null;
    }
}

