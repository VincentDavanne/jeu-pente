package uco.ima;

import java.awt.*;
import javax.swing.*;
import javax.swing.JOptionPane;

public class JFMain extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPGrid gameGrid;
    private Game game;
    private int size = 5 + 1; // Premier chiffre : taille de la grille
    private JPanel pnDroit;
    private JPanel pnChoix;
    private JLabel lblNewLabel;
    private JComboBox cbBlanc;
    private JLabel lblJoueurNoir;
    private JComboBox cbNoir;
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
    // JFMain.java
    public JFMain() {
        setTitle("Jeu de Pente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 800);
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // Création des joueurs avec la couleur spécifique
        AbstractPlayer whitePlayer = new HumanPlayer(PlayerColor.WHITE);  // Exemple : Humain avec la couleur blanche
        AbstractPlayer blackPlayer = new PlayerAIMinMax(PlayerColor.BLACK);    // Exemple : IA avec la couleur noire

        // Initialisation du jeu avec les joueurs
        game = new Game(size, whitePlayer, blackPlayer);
        gameGrid = new JPGrid(game, size);
        contentPane.add(gameGrid, BorderLayout.CENTER);

        // Panneau latéral pour les options de joueurs
        pnDroit = new JPanel();
        pnDroit.setPreferredSize(new Dimension(220, 10));
        contentPane.add(pnDroit, BorderLayout.EAST);
        pnDroit.setLayout(new BorderLayout(0, 0));

        // Panneau des choix (combobox + bouton)
        pnChoix = new JPanel();
        pnChoix.setPreferredSize(new Dimension(10, 140));
        pnDroit.add(pnChoix, BorderLayout.NORTH);
        pnChoix.setLayout(new GridLayout(3, 2, 5, 10));

        // Combobox pour le joueur blanc
        lblNewLabel = new JLabel("Joueur Blanc");
        pnChoix.add(lblNewLabel);

        cbBlanc = new JComboBox();
        cbBlanc.setModel(new DefaultComboBoxModel(new String[] {
                "Joueur IA", "Joueur Humain"
        }));
        pnChoix.add(cbBlanc);

        // Combobox pour le joueur noir
        lblJoueurNoir = new JLabel("Joueur Noir");
        pnChoix.add(lblJoueurNoir);

        cbNoir = new JComboBox();
        cbNoir.setModel(new DefaultComboBoxModel(new String[] {
                "Joueur IA", "Joueur Humain"
        }));
        pnChoix.add(cbNoir);

        // Bouton "Start"
        JButton btnStart = new JButton("Démarrer");
        pnChoix.add(new JLabel()); // Pour l'alignement
        pnChoix.add(btnStart);

        // Action du bouton "Start"
        btnStart.addActionListener(e -> startGame());
    }





    private void startGame() {
        // Récupère les choix des combobox
        boolean isWhiteHuman = cbBlanc.getSelectedItem().equals("Joueur Humain");
        boolean isBlackHuman = cbNoir.getSelectedItem().equals("Joueur Humain");

        // Créer les joueurs selon les choix
        AbstractPlayer whitePlayer;
        AbstractPlayer blackPlayer;

        if (isWhiteHuman) {
            whitePlayer = new HumanPlayer(PlayerColor.WHITE);  // Joueur blanc humain
        } else {
            whitePlayer = new PlayerAIMinMax(PlayerColor.WHITE);     // Joueur blanc IA
        }

        if (isBlackHuman) {
            blackPlayer = new HumanPlayer(PlayerColor.BLACK);  // Joueur noir humain
        } else {
            blackPlayer = new PlayerAIMinMax(PlayerColor.BLACK);     // Joueur noir IA
        }

        // Redémarre le jeu avec les nouveaux joueurs
        game = new Game(size, whitePlayer, blackPlayer);
        contentPane.remove(gameGrid);
        gameGrid = new JPGrid(game, size);
        contentPane.add(gameGrid, BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();

        // Si un des joueurs est une IA, lance un thread pour jouer automatiquement
        new Thread(() -> playGame(whitePlayer, blackPlayer)).start();
    }


    private void playGame(AbstractPlayer whitePlayer, AbstractPlayer blackPlayer) {
        while (!game.isOver()) {
            AbstractPlayer currentPlayer = game.getCurrentPlayer();

            if (currentPlayer instanceof PlayerAIMinMax) {
                // Jouer un coup automatiquement pour l'IA
                Position move = currentPlayer.getMove(game);
                game.makeMove(move);

                // Mettre à jour l'interface graphique
                SwingUtilities.invokeLater(() -> gameGrid.repaint());
            }

            try {
                Thread.sleep(500); // Pause pour fluidité
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
