package uco.ima;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class JPGrid extends JPanel {

    private static final long serialVersionUID = 1L;
    private int size, b, s, r;
    private Game game;           // Objet représentant la grille de jeu et contrôlant les déplacements
    private MouseAdapter mousePlay;

    /**
     * Create the panel.
     */
    public JPGrid(Game game, int size) {
        this.size = size;
        this.game = game;
        b = 10; // Bordure
        s = 33; // Taille des cases
        r = 12; // Rayon des pierres

        mousePlay = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                click(e);
            }
        };
        enableClick();
    }

    public void enableClick() {
        this.addMouseListener(mousePlay);
    }

    public void disableClick() {
        this.removeMouseListener(mousePlay);
    }

    private void click(MouseEvent e) {
        // Déterminer la position cliquée
        int x = e.getX();
        int y = e.getY();

// Calculer la position en indices sur la grille, en arrondissant au plus proche
        int j = Math.round((float)(x - b) / s);
        int i = Math.round((float)(y - b) / s);


        Position pos = new Position(i, j);

        // Vérifier si le mouvement est valide et l'appliquer
        if (game.isMoveValid(pos)) {
            game.makeMove(pos);
            repaint(); // Rafraîchit l'affichage de la grille

            if (game.isOver()) {
                repaint(); // Rafraîchir pour afficher le message de victoire
                System.out.println("Le match est terminé ! " + game.getWinner() + " a gagné !");
                disableClick();
            }
        } else {
            System.out.println("Coup invalide ! Réessayez.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessiner la grille
        int xMax = b + size * s, yMax = b + size * s;
        for (int y = b; y <= yMax; y += s) {
            g.drawLine(b, y, xMax, y);
        }
        for (int x = b; x <= xMax; x += s) {
            g.drawLine(x, b, x, yMax);
        }

        // Dessiner les pierres suivant leur position donnée dans la grille
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (game.getPion(i, j) == PlayerColor.BLACK) {
                    g.setColor(Color.BLACK);
                    g.fillOval(b + j * s - r, b + i * s - r, 2 * r, 2 * r);
                } else if (game.getPion(i, j) == PlayerColor.WHITE) {
                    g.setColor(Color.WHITE);
                    g.fillOval(b + j * s - r, b + i * s - r, 2 * r, 2 * r);
                }
            }
        }

        // Afficher les scores de captures
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.BLACK);
        g.drawString("Captures BLACK: " + game.getCaptures(PlayerColor.BLACK), b, yMax + 30);
        g.setColor(Color.WHITE);
        g.drawString("Captures WHITE: " + game.getCaptures(PlayerColor.WHITE), b, yMax + 50);

        // Afficher un message si le jeu est terminé
        if (game.isOver()) {
            g.setColor(Color.RED);
            g.drawString("Victoire de " + game.getWinner() + " !", b, yMax + 70);
        }
    }
}
