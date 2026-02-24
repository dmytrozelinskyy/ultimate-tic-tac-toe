import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class MainMenu extends JFrame{
    private JPanel backgroundPanel;
    private Image backgroundImage = new ImageIcon(getClass().getResource("/images/gradient-blue-pink.jpg")).getImage();
    private Image scaledImage;
    private JButton  startGame_Btn = createCustomButton("Start Game");
    private JButton settings_Btn = createCustomButton("Settings");
    private JButton scoreBoard_Btn = createCustomButton("Score Board");
    private JButton exit_Btn = createCustomButton("Exit");
    private ScoreBoardPopupMenu scoreBoardPopupMenu = new ScoreBoardPopupMenu();
    private SettingsPopupMenu settingsPopupMenu = new SettingsPopupMenu(scoreBoardPopupMenu, this);
    private StartGame startGamePanel;
    private GameOptions gameOptions;
    private GameMode gameMode;
    private static int width = 900;
    private static int height = 900;

    public MainMenu(){
        super();



        this.setTitle("Main Menu");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        backgroundPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if(backgroundImage != null){
                    g.drawImage(scaledImage, 0, 0,
                            getWidth(),
                            getHeight(),
                            this);
                }
            }
        };

        backgroundPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                width = getWidth();
                height = getHeight();
                scaledImage = backgroundImage.getScaledInstance(getWidth(),
                        getHeight(),
                        Image.SCALE_SMOOTH);
                scoreBoardPopupMenu.setSize(getWidth() / 2, getHeight() / 2);
                settingsPopupMenu.setSize(getWidth() / 2 + 100, getHeight() / 2 + 150);
                backgroundPanel.repaint();
            }
        });

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                settingsPopupMenu.setLocation(getLocation().x + getWidth() / 4 - 50  , getLocation().y + 200);
                scoreBoardPopupMenu.setLocation(getLocation().x + getWidth() / 4, getLocation().y + 200);
            }
        });
        startGamePanel = new StartGame(this);
        gameOptions = new GameOptions(startGamePanel, this);
        gameMode = gameOptions.getGameMode();

        backgroundPanel.setLayout(new BorderLayout());
        setButtons(backgroundPanel);
        setContentPane(backgroundPanel);
        this.setSize(width,height);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    public void setButtons(JPanel panel){
        JButton[] Buttons = {
                startGame_Btn,
                settings_Btn,
                scoreBoard_Btn,
                exit_Btn
        };
        // Adding buttons to the panel
        panel.add(Box.createRigidArea(new Dimension(0,height / 2 - 300)));
        for(var button : Buttons){
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(button);
            panel.add(Box.createRigidArea(new Dimension(0,30)));
        }
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Start Game Button Action Listener
        startGame_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameOptions.setSize(getWidth() / 2, getHeight() / 2);
                gameOptions.setLocation(getLocation().x + getWidth() / 4, getLocation().y + 200);
                gameOptions.setVisible(true);
            }
        });
        // Settings Button Action Listener
        settings_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsPopupMenu.setSize(getWidth() / 2 + 100, getHeight() / 2 + 150);
                settingsPopupMenu.setLocation(getLocation().x + getWidth() / 4 - 50  , getLocation().y + 200);
                settingsPopupMenu.setVisible(true);
            }
        });
        // Score Board Button Action Listener
        scoreBoard_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scoreBoardPopupMenu.setSize(getWidth() / 2, getHeight() / 2);
                scoreBoardPopupMenu.setLocation(getLocation().x + getWidth() / 4, getLocation().y + 200);
                scoreBoardPopupMenu.setVisible(true);
            }
        });
        // Exit Button Action Listener
        exit_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsPopupMenu.dispose();
                scoreBoardPopupMenu.dispose();
                gameOptions.dispose();
                setVisible(false);
                dispose();
            }
        });
    }

    // Creating Custom Buttons (with on_hover effect)
    private JButton createCustomButton(String text){
        Font customFont = new Font("Gill Sans", Font.BOLD, 32);
        JButton button = new JButton(text);
        button.setFont(customFont);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(163,157,157, 150));
        button.setBorder(BorderFactory.createEmptyBorder(30,70,30,70));

        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(115, 111, 111, 50));
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(163,157,157, 150));
                repaint();
            }
        });

        return button;
    }

    // Setters
    public void setScoreBoardPopupMenu(ScoreBoardPopupMenu scoreBoardPopupMenu){
        this.scoreBoardPopupMenu = scoreBoardPopupMenu;
    }

    // Getters
    public GameMode getGameMode(){return gameMode;}
    public JPanel getBackgroundPanel(){return backgroundPanel;}
    public GameOptions getGameOptions(){return gameOptions;}
    public SettingsPopupMenu getSettingsPopupMenu(){return settingsPopupMenu;}
    public ScoreBoardPopupMenu getScoreBoardPopupMenu(){return scoreBoardPopupMenu;}

    public static void main(String[] args){
        SwingUtilities.invokeLater(MainMenu::new);
    }

}
