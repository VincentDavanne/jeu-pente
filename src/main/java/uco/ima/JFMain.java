package uco.ima;

import java.awt.*;
import javax.swing.*;
import javax.swing.JOptionPane;

public class JFMain extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPGrid gameGrid;
    private Game game;
    private int size = 19;
    private JPanel pnDroit;
    private JPanel pnChoix;
    private JLabel lblNewLabel;
    private JComboBox cbBlanc;
    private JLabel lblJoueurNoir;
    private JComboBox cbNoir;
    //private AbstractPlayer whitePlayer, blackPlayer;// Taille de la grille

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
        pnDroit = new JPanel();
        pnDroit.setPreferredSize(new Dimension(220, 10));
        contentPane.add(pnDroit, BorderLayout.EAST);
        pnDroit.setLayout(new BorderLayout(0, 0));

        pnChoix = new JPanel();
        pnChoix.setPreferredSize(new Dimension(10, 100));
        pnDroit.add(pnChoix, BorderLayout.NORTH);
        pnChoix.setLayout(new GridLayout(2, 2, 0, 0));

        lblNewLabel = new JLabel("Joueur Blanc");
        pnChoix.add(lblNewLabel);

        cbBlanc = new JComboBox();
        cbBlanc.setModel(new DefaultComboBoxModel(new String[] {"Joueur IA Simple", "Joueur IA Min-Max", "Joueur Humain"}));
        pnChoix.add(cbBlanc);

        lblJoueurNoir = new JLabel("Joueur Noir");
        pnChoix.add(lblJoueurNoir);

        cbNoir = new JComboBox();
        cbNoir.setModel(new DefaultComboBoxModel(new String[] {"Joueur IA Simple", "Joueur IA Min-Max", "Joueur Humain"}));
        pnChoix.add(cbNoir);
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
        gameGrid.enableClick(); // Activer les clics pour les joueurs humains

        // Thread pour surveiller si le jeu est terminé
        new Thread(() -> {
            while (!game.isOver()) {
                if (game.isOver()) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this,
                                "Le match est terminé ! " + game.getWinner() + " a gagné !");
                    });
                    break;
                }

                try {
                    Thread.sleep(500); // Petite pause pour éviter des boucles intensives
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this,
                                "Le match est terminé ! " + game.getWinner() + " a gagné !");
                    });
                    break;
                }

                // Si c'est au tour de l'IA de jouer
                if (game.getCurrentPlayer() == PlayerColor.WHITE) {
                    Position aiMove = ai.getMove(game);
                    game.makeMove(aiMove);

                    // Mettre à jour l'interface graphique
                    SwingUtilities.invokeLater(() -> gameGrid.repaint());

                    // Vérifier si le jeu est terminé
                    if (game.isOver()) {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(this,
                                    "Le match est terminé ! " + game.getWinner() + " a gagné !");
                        });
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
