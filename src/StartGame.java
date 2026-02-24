import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.awt.event.KeyEvent.*;

public class StartGame extends JPanel implements KeyListener {
    private static final int GRID_SIZE = 3;
    private static PlayingSide playingSide = PlayingSide.X_PLAY;
    private static Map<Integer, String> panels_w = new HashMap<>();
    // --------------------------------------------------------------------------------------------------
    private static GamePanel[] panels = new GamePanel[GRID_SIZE * GRID_SIZE];
    private MainMenu mainMenu;
    private PausePopupMenu pausePopupMenu;
    private GameOptions gameOptions;
    private GameMode gameMode;
    private WinningPopup winningPopup;
    // --------------------------------------------------------------------------------------------------
    boolean isPause = false;
    int count_X = 0;
    int count_Y = 0;
    // --------------------------------------------------------------------------------------------------


    public StartGame(MainMenu mainMenu){
        super();

        this.mainMenu = mainMenu;
        this.gameOptions = mainMenu.getGameOptions();
        this.gameMode = GameMode.FRIEND;
        setComponents();
        this.setVisible(true);
    }


    private void setComponents(){

        this.setLayout(new BorderLayout());

        this.winningPopup = new WinningPopup(mainMenu, this);
        this.pausePopupMenu = new PausePopupMenu(this, mainMenu.getScoreBoardPopupMenu(), mainMenu);

        JPanel buttonPanel = new JPanel();
        JButton pause_btn = createCustomButton("Pause");
        JButton restart_btn = createCustomButton("Restart");
        buttonPanel.add(pause_btn);
        buttonPanel.add(restart_btn);

        pause_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pausePopupMenu.setSize(getWidth() / 2 + 100, getHeight() / 2 + 150);
                pausePopupMenu.setLocationRelativeTo(mainMenu);
                isPause = !isPause;
                pausePopupMenu.setVisible(isPause);
            }
        });

        restart_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        JPanel gamingPanel = new JPanel();
        gamingPanel.setLayout(new GridLayout(GRID_SIZE,GRID_SIZE));
        for(int i = 0; i < GRID_SIZE * GRID_SIZE; ++i){
            GamePanel gamePanel = new GamePanel(i);
            panels[i] = gamePanel;
            gamingPanel.add(gamePanel);
        }

        this.add(buttonPanel, BorderLayout.NORTH);
        this.add(gamingPanel, BorderLayout.CENTER);

        for (GamePanel panel : panels) {
            Button[][] buttons = panel.getButtons();
            for (int j = 0; j < GRID_SIZE; ++j)
                for (int k = 0; k < GRID_SIZE; ++k)
                    buttons[j][k].addActionListener(new buttonClickPerformed());
        }

        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
    }
    /**Method For Creating Custom Design For Buttons*/
    private void restartGame(){
        for (GamePanel panel : panels) {
            Button[][] buttons = panel.getButtons();
            for (int j = 0; j < GRID_SIZE; ++j) {
                for (int k = 0; k < GRID_SIZE; ++k) {
                    buttons[j][k].setText("");
                    buttons[j][k].setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
                    buttons[j][k].setEnabled(true);
                }
            }
            panel.setBackgroundImage(null);
        }
        panels_w.clear();
        winningPopup.setVisible(false);
        playingSide = PlayingSide.X_PLAY;
    }
    private JButton createCustomButton(String text){
        Font customFont;
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
    private class buttonClickPerformed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Button button = (Button) e.getSource();
            int btn_id = button.getBtn_id();
            for (int i = 0; i < panels.length; ++i) {
                Button[][] buttons = panels[i].getButtons();
                for (int j = 0; j < GRID_SIZE; ++j) {
                    for (int k = 0; k < GRID_SIZE; ++k) {
                        if (panels_w.containsKey(btn_id)) {
                            continue;
                        }
                        if (i == btn_id) {
                            buttons[j][k].setBorder(BorderFactory.createLineBorder(mainMenu.getSettingsPopupMenu().getChosenColor(), 4));
                            if (buttons[j][k].getText().isEmpty()) {
                                buttons[j][k].setEnabled(true);
                            }
                        } else {
                            buttons[j][k].setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
                            buttons[j][k].setEnabled(false);
                        }
                    }
                }
            }
            if (button.getText().isEmpty()) {
                if (playingSide == PlayingSide.X_PLAY) {
                    if(gameMode == GameMode.FRIEND){
                        button.setText("X");
                        playingSide = PlayingSide.O_PLAY;
                    }
                    else{
                        button.setText("X");
                        int currentPanelIndex = getPanelIndexButtonPressedOn(btn_id);

                        if (isPanelFinished(currentPanelIndex)) {
                            int nextPanelIndex = findNextAvailablePanel();
                            if (nextPanelIndex != -1)
                                makePCMove(nextPanelIndex);
                            else System.out.println("No available panels");
                        } else if (!isPanelFinished(currentPanelIndex)) {
                            makePCMove(btn_id);
                        }
                        playingSide = PlayingSide.X_PLAY;
                    }
                } else if (playingSide == PlayingSide.O_PLAY) {
                    if (gameMode == GameMode.FRIEND) {
                        button.setText("O");
                        playingSide = PlayingSide.X_PLAY;
                    } else if (gameMode == GameMode.PC) {

                    }
                }
            }
            for (int i = 0; i < panels.length; ++i) {
                if (checkWinningCombinations(panels[i], "X")) {
                    panels[i].setBackgroundImage(new ImageIcon("..\\UltimateTicTacToe\\resources\\images\\letter-x.png").getImage());
                    panels_w.put(i, "X");
                } else if (checkWinningCombinations(panels[i], "O")) {
                    panels[i].setBackgroundImage(new ImageIcon("..\\UltimateTicTacToe\\resources\\images\\letter-o.png").getImage());
                    panels_w.put(i, "O");
                }
            }

            if (panels_w.get(0) != null && panels_w.get(0).equals("X") && panels_w.get(1) != null && panels_w.get(1).equals("X") && panels_w.get(2) != null && panels_w.get(2).equals("X") ||             //rows
                    panels_w.get(3) != null && panels_w.get(3).equals("X") && panels_w.get(4) != null && panels_w.get(4).equals("X") && panels_w.get(5) != null && panels_w.get(5).equals("X") ||
                    panels_w.get(6) != null && panels_w.get(6).equals("X") && panels_w.get(7) != null && panels_w.get(7).equals("X") && panels_w.get(8) != null && panels_w.get(8).equals("X") ||
                    panels_w.get(0) != null && panels_w.get(0).equals("X") && panels_w.get(3) != null && panels_w.get(3).equals("X") && panels_w.get(6) != null && panels_w.get(6).equals("X") ||             //cols
                    panels_w.get(1) != null && panels_w.get(1).equals("X") && panels_w.get(4) != null && panels_w.get(4).equals("X") && panels_w.get(7) != null && panels_w.get(7).equals("X") ||
                    panels_w.get(2) != null && panels_w.get(2).equals("X") && panels_w.get(5) != null && panels_w.get(5).equals("X") && panels_w.get(8) != null && panels_w.get(8).equals("X") ||
                    panels_w.get(0) != null && panels_w.get(0).equals("X") && panels_w.get(4) != null && panels_w.get(4).equals("X") && panels_w.get(8) != null && panels_w.get(8).equals("X") ||             //diagonals
                    panels_w.get(2) != null && panels_w.get(2).equals("X") && panels_w.get(4) != null && panels_w.get(4).equals("X") && panels_w.get(6) != null && panels_w.get(6).equals("X")
            ) {
                System.out.println("X WINS");
                setCount_X(count_X += 3);
                winningPopup.setCount_X(count_X);
                winningPopup.setCount_Y(count_Y);
                winningPopup.setSize(getWidth() / 2 + 100, getHeight() / 2 + 150);
                winningPopup.setLocationRelativeTo(mainMenu);
                winningPopup.setVisible(true);
            } else if (panels_w.get(0) != null && panels_w.get(0).equals("O") && panels_w.get(1) != null && panels_w.get(1).equals("O") && panels_w.get(2) != null && panels_w.get(2).equals("O") ||             //rows
                    panels_w.get(3) != null && panels_w.get(3).equals("O") && panels_w.get(4) != null && panels_w.get(4).equals("O") && panels_w.get(5) != null && panels_w.get(5).equals("O") ||
                    panels_w.get(6) != null && panels_w.get(6).equals("O") && panels_w.get(7) != null && panels_w.get(7).equals("O") && panels_w.get(8) != null && panels_w.get(8).equals("O") ||
                    panels_w.get(0) != null && panels_w.get(0).equals("O") && panels_w.get(3) != null && panels_w.get(3).equals("O") && panels_w.get(6) != null && panels_w.get(6).equals("O") ||             //cols
                    panels_w.get(1) != null && panels_w.get(1).equals("O") && panels_w.get(4) != null && panels_w.get(4).equals("O") && panels_w.get(7) != null && panels_w.get(7).equals("O") ||
                    panels_w.get(2) != null && panels_w.get(2).equals("O") && panels_w.get(5) != null && panels_w.get(5).equals("O") && panels_w.get(8) != null && panels_w.get(8).equals("O") ||
                    panels_w.get(0) != null && panels_w.get(0).equals("O") && panels_w.get(4) != null && panels_w.get(4).equals("O") && panels_w.get(8) != null && panels_w.get(8).equals("O") ||             //diagonals
                    panels_w.get(2) != null && panels_w.get(2).equals("O") && panels_w.get(4) != null && panels_w.get(4).equals("O") && panels_w.get(6) != null && panels_w.get(6).equals("O")
            ) {
                System.out.println("Y WINS");
                setCount_Y(count_Y += 3);
                winningPopup.setCount_X(count_X);
                winningPopup.setCount_Y(count_Y);
                winningPopup.setSize(getWidth() / 2 + 100, getHeight() / 2 + 150);
                winningPopup.setLocationRelativeTo(mainMenu);
                winningPopup.setVisible(true);
            }
        }

        private int getPanelIndexButtonPressedOn(int btn_id){
            return btn_id / (GRID_SIZE * GRID_SIZE);
        }
        private boolean isPanelFinished(int panelIndex){
            return panels_w.containsKey(panelIndex);
        }
        private int findNextAvailablePanel(){
            for(int i = 0; i < panels.length; ++i){
                if(!isPanelFinished(i))
                    return i;
            }
            return -1;
        }
        private void makePCMove(int panelIndex){
            boolean PC_Moved = false;
            GamePanel nextPanel = panels[panelIndex];
            Button[][] buttons = nextPanel.getButtons();
            for (int j = 0; j < GRID_SIZE; ++j) {
                int Xcount = 0;
                int placableIndex = -1;
                for (int k = 0; k < GRID_SIZE; ++k) {
                    if (buttons[j][k].getText().equals("X")) {
                        Xcount++;
                    } else if (buttons[j][k].getText().equals("")) {
                        placableIndex = k;
                    }
                }
                if (Xcount == GRID_SIZE - 1 && placableIndex != -1) {
                    buttons[j][placableIndex].setText("O");
                    PC_Moved = true;
                    break;
                }
            }


            if(!PC_Moved){
                for (int k = 0; k < GRID_SIZE; ++k) {
                    int Xcount = 0;
                    int placableIndex = -1;
                    for (int j = 0; j < GRID_SIZE; ++j) {
                        if (buttons[j][k].getText().equals("X")) {
                            Xcount++;
                        } else if (buttons[j][k].getText().equals("")) {
                            placableIndex = j;
                        }
                        if (Xcount == GRID_SIZE - 1 && placableIndex != -1) {
                            buttons[placableIndex][k].setText("O");
                            PC_Moved = true;
                            break;
                        }
                    }
                }

                if(!PC_Moved){
                    int Xcount = 0;
                    int emptyRowIndex = -1;
                    int emptyColIndex = -1;
                    for (int j = 0; j < GRID_SIZE; ++j) {
                        if (buttons[j][j].getText().equals("X")) {
                            Xcount++;
                        } else if (buttons[j][j].getText().equals("")) {
                            emptyRowIndex = j;
                            emptyColIndex = j;
                        }
                    }
                    if (Xcount == GRID_SIZE - 1 && emptyRowIndex != -1 && emptyColIndex != -1) {
                        buttons[emptyRowIndex][emptyColIndex].setText("O");
                        PC_Moved = true;
                    }
                }

                if(!PC_Moved){
                    int Xcount = 0;
                    int emptyRowIndex = -1;
                    int emptyColIndex = -1;
                    for (int j = 0; j < GRID_SIZE; ++j) {
                        if (buttons[j][GRID_SIZE - j - 1].getText().equals("X")) {
                            Xcount++;
                        } else if (buttons[j][GRID_SIZE - j - 1].getText().equals("")) {
                            emptyRowIndex = j;
                            emptyColIndex = GRID_SIZE - j - 1;
                        }
                    }
                    if (Xcount == GRID_SIZE - 1 && emptyRowIndex != -1 && emptyColIndex != -1) {
                        buttons[emptyRowIndex][emptyColIndex].setText("O");
                        PC_Moved = true;
                    }
                }

                if (!PC_Moved) {
                    for (int j = 0; j < GRID_SIZE; ++j) {
                        for (int k = 0; k < GRID_SIZE; ++k) {
                            if (buttons[j][k].getText().equals("")) {
                                buttons[j][k].setText("O");
                                PC_Moved = true;
                                break;
                            }
                        }
                        if (PC_Moved) break;
                    }
                }
            }
        }
        private boolean checkWinningCombinations(GamePanel panel, String side){
            Button[][] buttons = panel.getButtons();
            // Checking Rows
            for(int i = 0; i < 3; ++i)
                if(buttons[i][0].getText().equals(side) && buttons[i][1].getText().equals(side) && buttons[i][2].getText().equals(side))return true;
            // Checking Cols
            for(int i = 0; i < 3; ++i)
                if(buttons[0][i].getText().equals(side) && buttons[1][i].getText().equals(side) && buttons[2][i].getText().equals(side)) return true;
            // Checking Diagonals
            if (buttons[0][0].getText().equals(side) && buttons[1][1].getText().equals(side) && buttons[2][2].getText().equals(side)) return true;
            if (buttons[0][2].getText().equals(side) && buttons[1][1].getText().equals(side) && buttons[2][0].getText().equals(side)) return true;
            return false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == VK_ESCAPE && !pausePopupMenu.isVisible())
            pausePopupMenu.setVisible(true);
        else if(e.getKeyCode() == VK_ESCAPE)
            pausePopupMenu.setVisible(false);
    }


    public GamePanel[] getPanels(){return panels;}
    public int getGridSize(){return GRID_SIZE;}

    public PlayingSide getPlayingSide(){return playingSide;}
    public Map<Integer, String> getPanels_w (){return panels_w;}
    public void setPlayingSide(PlayingSide playingSide){this.playingSide = playingSide;}
    public void setPause(boolean isPause){
        this.isPause = isPause;
    }
    public void setCount_X(int count_X){
        this.count_X = count_X;
    }
    public void setCount_Y(int count_Y){
        this.count_Y = count_Y;
    }

    public MainMenu getMainMenu() {
            return mainMenu;
    }
    public boolean getIsPause(){return isPause;}

}
