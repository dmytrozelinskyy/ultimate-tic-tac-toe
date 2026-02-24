import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.channels.FileChannel;


public class SettingsPopupMenu extends JWindow {
    private JPanel settingsPanel = new JPanel();
    private JPanel activePanelColorSelectionPanel = new JPanel();
    private JLabel activePanelColorSelectionTitle = new JLabel("Select color for active panel (panel that can be played on): ");
    private JLabel clearScoreBoardTitle = new JLabel("Clear Score Board Data: ");


    private JButton clearScoreBoard = new JButton("Clear Score Board");
    private JColorChooser colorChooser = new JColorChooser();
    private ScoreBoardPopupMenu scoreBoardPopupMenu;
    private MainMenu mainMenu;


    /**Default Constructor (Creation of Score Board Menu)*/
    public SettingsPopupMenu(ScoreBoardPopupMenu scoreBoardPopupMenu, MainMenu mainMenu){
        super();
        this.setSize(700,900);
        this.getRootPane()
                .setBorder(BorderFactory.createMatteBorder(3,3,3,3,Color.BLACK));
        this.mainMenu = mainMenu;
        this.scoreBoardPopupMenu = scoreBoardPopupMenu;
        setComponents();
        this.add(settingsPanel);
        colorChooser.setColor(Color.RED);
        this.setVisible(false);
    }

    public void setComponents(){
        settingsPanel.setLayout(new BorderLayout());

        JButton exitButton = new JButton(new ImageIcon("..\\UltimateTicTacToe\\resources\\images\\close.png"));
        JLabel title = new JLabel("Settings");
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel();


        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        title.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 24));
        buttonPanel.add(exitButton, BorderLayout.EAST);
        buttonPanel.add(title, BorderLayout.CENTER);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(0,0,2,0,Color.BLACK));

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        settingsPanel.add(buttonPanel, BorderLayout.NORTH);


        activePanelColorSelectionPanel.setLayout(new BorderLayout());
        activePanelColorSelectionPanel.add(activePanelColorSelectionTitle, BorderLayout.NORTH);
        activePanelColorSelectionPanel.add(colorChooser, BorderLayout.CENTER);

        settingsPanel.add(activePanelColorSelectionPanel, BorderLayout.CENTER);

        clearScoreBoard.setFont(new Font("Arial", Font.PLAIN,20));
        clearScoreBoard.setContentAreaFilled(false);
        clearScoreBoard.setFocusPainted(false);
        clearScoreBoard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        clearScoreBoard.setCursor(new Cursor(Cursor.HAND_CURSOR));

        clearScoreBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearScoreBoard();
                scoreBoardPopupMenu = new ScoreBoardPopupMenu();
                mainMenu.setScoreBoardPopupMenu(scoreBoardPopupMenu);
            }
        });

        bottomPanel.add(clearScoreBoardTitle);
        bottomPanel.add(clearScoreBoard);
        settingsPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void clearScoreBoard(){
        String filePath = "..\\UltimateTicTacToe\\results\\ScoreBoardResults.txt";
        try{
            File file = new File(filePath);
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            FileChannel channel = raf.getChannel();

            // Truncate the file to delete its content
            channel.truncate(0);

            raf.close();
            channel.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Color getChosenColor(){
        return colorChooser.getColor();
    }
}

