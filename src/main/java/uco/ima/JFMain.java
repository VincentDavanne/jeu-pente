package uco.ima;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JFMain extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPGrid gameGrid;
    private Game game;
    private int size = 19; // Taille de la grille

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                JFMain frame = new JFMain();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public JFMain() {
        setTitle("Jeu de Pente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 800);
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // Initialisation du jeu
        game = new Game(size);
        gameGrid = new JPGrid(game, size);
        contentPane.add(gameGrid, BorderLayout.CENTER);

        // Panneau des options
        JPanel optionsPanel = new JPanel(new GridLayout(1, 3));

        // Bouton pour redémarrer le jeu
        JButton btnRestart = new JButton("Redémarrer");
        btnRestart.addActionListener(e -> restartGame());
        optionsPanel.add(btnRestart);

        // Bouton pour Humain vs Humain
        JButton btnHvsH = new JButton("Humain vs Humain");
        btnHvsH.addActionListener(e -> startHumanVsHuman());
        optionsPanel.add(btnHvsH);

        // Bouton pour Humain vs IA
        JButton btnHvsAI = new JButton("Humain vs IA");
        btnHvsAI.addActionListener(e -> startHumanVsAI());
        optionsPanel.add(btnHvsAI);

        contentPane.add(optionsPanel, BorderLayout.SOUTH);
    }

    private void restartGame() {
        game = new Game(size);
        contentPane.remove(gameGrid);
        gameGrid = new JPGrid(game, size);
        contentPane.add(gameGrid, BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();
    }

    private void startHumanVsHuman() {
        restartGame();
        System.out.println("Mode Humain vs Humain activé !");
    }

    private void startHumanVsAI() {
        restartGame();
        AIPlayer ai = new AIPlayer(PlayerColor.WHITE);

        // Activer un écouteur pour les mouvements humains
        gameGrid.enableClick();

        // Thread pour gérer les mouvements IA
        new Thread(() -> {
            while (!game.isOver()) {
                if (game.isOver()) {
                    System.out.println("Le match est terminé ! " + game.getWinner() + " a gagné !");
                    break;
                }

                // Si c'est au tour de l'IA de jouer
                if (game.getCurrentPlayer() == PlayerColor.WHITE) {
                    Position aiMove = ai.getMove(game);
                    game.makeMove(aiMove);

                    // Mettre à jour l'interface graphique
                    gameGrid.repaint();

                    // Vérifier si le jeu est terminé
                    if (game.isOver()) {
                        System.out.println("Le match est terminé ! WHITE a gagné !");
                        break;
                    }
                }

                try {
                    Thread.sleep(500); // Petite pause pour éviter des actions immédiates
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
