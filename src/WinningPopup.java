import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


public class WinningPopup extends JDialog {
    private JTextField x_inputSaveName = new JTextField(20);
    private JTextField y_inputSaveName = new JTextField(20);
    private JButton S_C_Btn = new JButton("Save and Continue");
    private JButton S_E_Btn = new JButton("Save and Exit");
    private String filePath = "";
    private String textToWrite = "";
    private StartGame startGame;
    private MainMenu mainFrame;
    public PausePopupMenu pausePopupMenu;
    private int count_X;
    private int count_Y;


    public WinningPopup(MainMenu frame_p, StartGame startGame){
        super(frame_p, "Save Game", ModalityType.APPLICATION_MODAL);

        this.startGame = startGame;
        this.mainFrame = frame_p;
        this.pausePopupMenu = pausePopupMenu;

        setComponents();
        this.setVisible(false);
    }

    public void setComponents(){
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonSectionPanel = new JPanel();
        JPanel inputSectionPanel = new JPanel();

        inputSectionPanel.setLayout(new BoxLayout(inputSectionPanel, BoxLayout.Y_AXIS));
        inputSectionPanel.add(new Label("X Enter Name: "));
        inputSectionPanel.add(x_inputSaveName);
        inputSectionPanel.add(new Label("Y Enter Name: "));
        inputSectionPanel.add(y_inputSaveName);

        buttonSectionPanel.add(S_C_Btn);
        buttonSectionPanel.add(S_E_Btn);

        mainPanel.add(inputSectionPanel, BorderLayout.CENTER);
        mainPanel.add(buttonSectionPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
        this.pack();                                                                                          // size compresses to fit all elements
        this.getRootPane()
                .setBorder(BorderFactory.createMatteBorder(3,3,3,3, Color.BLACK));

        S_C_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GamePanel[] panels = startGame.getPanels();
                Map<Integer, String> panels_w = startGame.getPanels_w();
                for(int i = 0; i < panels.length;  ++i){
                    Button[][] buttons = panels[i].getButtons();
                    for(int j = 0; j < startGame.getGridSize(); ++j){
                        for(int k = 0; k < startGame.getGridSize(); ++k){
                            buttons[j][k].setText("");
                            buttons[j][k].setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
                            buttons[j][k].setEnabled(true);
                        }
                    }
                    panels[i].setBackgroundImage(null);
                }
                panels_w.clear();
                setVisible(false);
                startGame.setPlayingSide(PlayingSide.X_PLAY);
            }
        });
        S_E_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveResults(x_inputSaveName.getText(), count_X);
                saveResults(y_inputSaveName.getText(), count_Y);
                for(int i = 0; i < startGame.getPanels().length;  ++i){
                    Button[][] buttons =  startGame.getPanels()[i].getButtons();
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
                setVisible(false);
                startGame.getMainMenu().setContentPane(startGame.getMainMenu().getBackgroundPanel());
            }
        });

    }


    private void saveResults(String name, int score){
        LocalDateTime date = LocalDateTime.now();

        // Format the date to [dd-MM-yyyy] format
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = date.format(dateFormatter);

        String content = name + " | [" + formattedDate + "] | " + score;

        String fileName = "..\\UltimateTicTacToe\\results\\ScoreBoardResults.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(content);
            writer.newLine(); // Add a new line for next entry
            System.out.println("Content appended to " + fileName);
        } catch (IOException e) {
            System.err.println("Error appending to file " + fileName + ": " + e.getMessage());
        }
    }

    // Setters
    public void setCount_X(int count_X){
        this.count_X = count_X;
    }
    public void setCount_Y(int count_Y){
        this.count_Y = count_Y;
    }

    // Getters
    public int getCount_X(){return count_X;}
    public int getCount_Y(){return count_Y;}
}