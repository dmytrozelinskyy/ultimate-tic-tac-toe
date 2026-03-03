import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOptions extends JWindow {
    private JPanel optionsPanel = new JPanel();
    private JButton playWithFriend_btn = new JButton("Play with Friend",
            new ImageIcon("..\\UltimateTicTacToe\\resources\\images\\games.png"));
    private JButton playWithPC_btn = new JButton("Play with PC (soon...)",
            new ImageIcon("..\\UltimateTicTacToe\\resources\\images\\pc_ttt.png"));

    private GameMode gameMode;
    private MainMenu mainMenu;
    private StartGame startGame;


    /**Default Constructor (Game Mode Choose Menu)*/
    public GameOptions(StartGame startGame, MainMenu mainMenu){
        super();

        this.startGame = startGame;
        this.mainMenu = mainMenu;

        setComponents();
        this.setVisible(false);
    }

    /**Setting Components Of Game Mode Chooser Menu*/
    private void setComponents(){
        optionsPanel.setLayout(new GridLayout());

        JButton exitButton = new JButton(new ImageIcon("..\\UltimateTicTacToe\\resources\\images\\close.png"));           // 'X' close button
        JLabel title = new JLabel("Choose Game Mode");
        JPanel buttonPanel = new JPanel(new BorderLayout());

        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        title.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 24));
        buttonPanel.add(exitButton, BorderLayout.EAST);
        buttonPanel.add(title, BorderLayout.CENTER);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(0,0,2,0,Color.BLACK));

        exitButton.addActionListener(e -> setVisible(false));

        setButton(playWithFriend_btn, GameMode.FRIEND, "Ultimate Tic Tac Toe(Friend Mode)");

        playWithPC_btn.setEnabled(false);
        setButton(playWithPC_btn, GameMode.PC, "Ultimate Tic Tac Toe(PC Mode)");

        optionsPanel.add(playWithFriend_btn);
        optionsPanel.add(playWithPC_btn);

        this.add(buttonPanel, BorderLayout.NORTH);
        this.add(optionsPanel, BorderLayout.CENTER);
    }

    /**Set Buttons Method*/
    public void setButton(JButton button, GameMode mode, String title){
        button.setBackground(Color.WHITE);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                button.setBackground(new Color(98, 37, 143, 70));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameMode = mode;
                setVisible(false);
                mainMenu.setContentPane(startGame);
                mainMenu.setTitle(title);
                mainMenu.revalidate();
                mainMenu.repaint();
            }
        });
    }

    /**Getters*/
    public GameMode getGameMode(){return gameMode;}
}
