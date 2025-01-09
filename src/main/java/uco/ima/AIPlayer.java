package uco.ima;

import java.util.Random;

public class AIPlayer {
    private PlayerColor aiColor;
    private Random random;

    public AIPlayer(PlayerColor aiColor) {
        this.aiColor = aiColor;
        this.random = new Random();
    }

    public Position getMove(Game game) {
        // Chercher un mouvement stratégique
        //for (int i = 0; i < game.getSize(); i++) {
         //   for (int j = 0; j < game.getSize(); j++) {
           //     Position pos = new Position(i, j);
             //   if (game.isMoveValid(pos)) {
               //     return pos; // Premier coup valide trouvé
                //}
            //}
        //}

        // Si aucun mouvement stratégique trouvé, choisir aléatoirement
        int x, y;
        do {
            x = random.nextInt(game.getSize());
            y = random.nextInt(game.getSize());
        } while (!game.isMoveValid(new Position(x, y)));

        return new Position(x, y);
    }
}
