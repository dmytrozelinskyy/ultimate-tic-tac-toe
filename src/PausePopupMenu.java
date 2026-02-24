import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.interfaces.DSAKey;
import java.util.ArrayList;
import java.util.List;

public class PausePopupMenu extends JWindow {

    private JPanel pausePanel = new JPanel();
    private StartGame startGame;
    private SettingsPopupMenu settingsPopupMenu;
    private ScoreBoardPopupMenu scoreBoardPopupMenu;
    private MainMenu mainMenu;
    private SavePopupMenu savePopupMenu;
    private LoadPopupMenu loadPopupMenu;

    JButton continue_btn = createCustomButton("Continue");
    JButton save_btn = createCustomButton("Save");
    JButton load_btn = createCustomButton("Load");
    JButton scoreBoard_btn = createCustomButton("Score Board");
    JButton mainMenu_btn = createCustomButton("Main Menu");



    /**Default Constructor (Creation of Score Board Menu)*/
    public PausePopupMenu(StartGame startGame,ScoreBoardPopupMenu scoreBoardPopupMenu, MainMenu mainMenu){
        super();

        this.startGame = startGame;
        this.scoreBoardPopupMenu = scoreBoardPopupMenu;
        this.mainMenu = mainMenu;
        this.setSize(700,800);
        this.getRootPane()
                .setBorder(BorderFactory.createMatteBorder(3,3,3,3,Color.BLACK));
        pausePanel.setLayout(new BorderLayout());
        savePopupMenu = new SavePopupMenu(mainMenu, startGame, this);
        loadPopupMenu = new LoadPopupMenu(mainMenu, startGame);
        setContent();
        setContentPane(pausePanel);
        this.setVisible(false);
    }


    private void setContent(){
        JButton[] buttons = {
                continue_btn,
                save_btn,
                load_btn,
                scoreBoard_btn,
                mainMenu_btn
        };


        continue_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame.isPause = !startGame.isPause;
                setVisible(false);
            }
        });
        save_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePopupMenu.setSize(getWidth() / 2, getHeight() / 2);
                savePopupMenu.setLocation(getLocation().x + getWidth() / 4, getLocation().y + 200);
                savePopupMenu.setVisible(true);
            }
        });
        load_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPopupMenu.setSize(getWidth() / 2, getHeight() / 2);
                loadPopupMenu.setLocation(getLocation().x + getWidth() / 4, getLocation().y + 200);
                loadPopupMenu.setVisible(true);
            }
        });
        scoreBoard_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scoreBoardPopupMenu.setVisible(true);
            }
        });
        mainMenu_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToMainMenu();
                setVisible(false);
                mainMenu.setContentPane(mainMenu.getBackgroundPanel());
            }
        });


        pausePanel.add(Box.createRigidArea(new Dimension(0,50)));
        for(var button : buttons){
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setAlignmentX(CENTER_ALIGNMENT);
            pausePanel.add(button);
            pausePanel.add(Box.createRigidArea(new Dimension(0,20)));
        }
        pausePanel.setLayout(new BoxLayout(pausePanel, BoxLayout.Y_AXIS));
    }

    public void goToMainMenu(){
        for(int i = 0; i < startGame.getPanels().length;  ++i){
            Button[][] buttons = startGame.getPanels()[i].getButtons();
            for(int j = 0; j <  startGame.getGridSize(); ++j){
                for(int k = 0; k <  startGame.getGridSize(); ++k){
                    buttons[j][k].setText("");
                    buttons[j][k].setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
                    buttons[j][k].setEnabled(true);
                }
            }
        }
        startGame.setPlayingSide(PlayingSide.X_PLAY);
        startGame.isPause = !startGame.isPause;
    }


    private JButton createCustomButton(String text){
        Font customFont = null;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("..\\UltimateTicTacToe\\resources\\fonts\\Inter-SemiBold.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        }catch (IOException e){
            e.printStackTrace();
        }catch (FontFormatException e){
            e.printStackTrace();
        }
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(163,157,157, 150));
        button.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));

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


    
}
