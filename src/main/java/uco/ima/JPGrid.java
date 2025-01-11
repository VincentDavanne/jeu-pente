package uco.ima;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class JPGrid extends JPanel {

    private static final long serialVersionUID = 1L;
    private int size, b, s, r;
    private Game game;           // Objet représentant la grille de jeu
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
        // Coordonnées du clic en pixels
        int x = e.getX();
        int y = e.getY();

        // -----------------------------
        // 2ème solution : Math.round + clamp
        // -----------------------------
        // 1) On calcule l'indice avec Math.round
        int j = Math.round((float)(x - b) / s);
        int i = Math.round((float)(y - b) / s);


        // On crée la Position : (ligne = i, colonne = j)
        Position pos = new Position(i, j);

        // Vérifier si le coup est valide et l'appliquer
        if (game.isMoveValid(pos)) {
            game.makeMove(pos);
            repaint();

            if (game.isOver()) {
                repaint();
                JOptionPane.showMessageDialog(this,
                        "Le match est terminé ! " + game.getWinner() + " a gagné !");
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
        //size - 1 pour pouvoir jouer sur la dernière ligne/colonne
        int xMax = b + (size-1) * s;
        int yMax = b + (size-1) * s;
        for (int y = b; y <= yMax; y += s) {
            g.drawLine(b, y, xMax, y);
        }
        for (int x = b; x <= xMax; x += s) {
            g.drawLine(x, b, x, yMax);
        }

        // Dessiner les pierres
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                PlayerColor pion = game.getPion(i, j);
                if (pion == PlayerColor.BLACK) {
                    g.setColor(Color.BLACK);
                    g.fillOval(b + j * s - r, b + i * s - r, 2 * r, 2 * r);
                } else if (pion == PlayerColor.WHITE) {
                    g.setColor(Color.WHITE);
                    g.fillOval(b + j * s - r, b + i * s - r, 2 * r, 2 * r);
                }
            }
        }

        // Afficher le score de captures
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.BLACK);
        g.drawString("Captures BLACK: " + game.getCaptures(PlayerColor.BLACK), b, yMax + 30);
        g.setColor(Color.WHITE);
        g.drawString("Captures WHITE: " + game.getCaptures(PlayerColor.WHITE), b, yMax + 50);

        // Si partie terminée, afficher un message
        if (game.isOver()) {
            g.setColor(Color.RED);
            g.drawString("Victoire de " + game.getWinner() + " !", b, yMax + 70);
        }
    }
}
