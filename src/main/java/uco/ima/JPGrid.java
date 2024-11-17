package uco.ima;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class JPGrid extends JPanel {

    private static final long serialVersionUID = 1L;
    private int size, b, s, r;
    private Game game;				// objet représentant la grille de jeu et contrôlant les déplacements
    private boolean firstClick;
    private Position start;			// position de la case de départ
    private MouseAdapter mousePlay;

    /**
     * Create the panel.
     */
    public JPGrid(Game game, int size) {
        this.size = size;
        this.game = game;
        b = 10; s = 33; r = 12;
        firstClick = true;
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

    public void disenableClick() {
        this.removeMouseListener(mousePlay);
    }
    private void click(MouseEvent e) {
//		Déterminer le numéro de ligne i et le numéro de colonne j de la case cliquée
        int j = (e.getX() - b) / s;
        int i = (e.getY() - b) / s;
        if (firstClick) {
            // Retenir le pion cliqué
            start = new Position(i, j);
        } else {
            Position end = new Position(i, j);
            Move move = new Move(start, end);
            if (game.moveValid(move)) {
                game.makeMove(move);
                repaint();
                if (game.isOver()) {
                    System.out.println("Le match a fini ! " + game.getWinner() + " a gagné !");
                    disenableClick();
                }
            } else {
                System.out.println("Movement invalide ! Recommencez !");
            }
        }
        firstClick = !firstClick;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dessiner la grille
        int xMax = b + size*s, yMax = b + size*s;
        for (int y = b; y <= yMax; y += s)
            g.drawLine(b, y, xMax, y);
        for (int x = b; x <= xMax; x += s)
            g.drawLine(x, b, x, yMax);
        // Dessiner les pions suivant leur position donnée dans le tableau pion
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (game.getPion(i, j) == PlayerColor.BLACK) {	// pion noir
                    // Dessiner un pion noir à la case (i,j)
                    g.setColor(Color.BLACK);
                    g.fillOval(b + j * s + s/2 - r, b + i * s + s/2 - r, 2*r, 2*r);
                } else if (game.getPion(i, j) == PlayerColor.WHITE) {
                    // Dessiner un pion blanc à la case (i,j)
                    g.setColor(Color.WHITE);
                    g.fillOval(b + j * s + s/2 - r, b + i * s + s/2 - r, 2*r, 2*r);
                }
            }
    }
}
