import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SavePopupMenu extends JDialog {
    private JTextField inputSaveName = new JTextField(20);
    private JButton saveButton = new JButton("Save");
    private JButton cancelButton = new JButton("Cancel");
    private LocalDateTime currentDate = LocalDateTime.now();
    private String filePath = "";
    private String textToWrite = "";
    private StartGame startGame;
    private MainMenu mainFrame;
    public PausePopupMenu pausePopupMenu;


    public SavePopupMenu(MainMenu frame_p, StartGame startGame, PausePopupMenu pausePopupMenu){
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

        inputSectionPanel.add(new Label("Save Name: "));
        inputSectionPanel.add(inputSaveName);

        buttonSectionPanel.add(saveButton);
        buttonSectionPanel.add(cancelButton);

        mainPanel.add(inputSectionPanel, BorderLayout.CENTER);
        mainPanel.add(buttonSectionPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
        this.pack();                                                                                          // size compresses to fit all elements
        this.getRootPane()
                .setBorder(BorderFactory.createMatteBorder(3,3,3,3, Color.BLACK));

        saveButton.addActionListener(e -> {
            if(inputSaveName.getText().isEmpty()){
                JDialog errorDialogWindow = new JDialog(mainFrame, "Error occurred", ModalityType.APPLICATION_MODAL);
                errorDialogWindow.setSize(300,200);
                errorDialogWindow.setLocationRelativeTo(mainFrame);
                errorDialogWindow.add(new JLabel("Error! Incorrect save name!"));
                errorDialogWindow.setVisible(true);
            }
            else{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMMyyyy");
                filePath += "save_" + currentDate.format(formatter) + "_" + inputSaveName.getText() + ".txt";

                textToWrite += startGame.getPlayingSide() + "\n";
                for(var element : startGame.getPanels_w().entrySet())
                    textToWrite += element.getKey() + " | " + element.getValue() + '\n';
                GamePanel[] panels = startGame.getPanels();
                for(int i = 0; i < panels.length; ++i){
                    Button[][] buttons = panels[i].getButtons();
                    for(int j = 0; j < startGame.getGridSize(); ++j){
                        for (int k = 0; k < startGame.getGridSize(); ++k){
                            if(buttons[j][k].getText().isEmpty()) continue;
                            textToWrite += buttons[j][k].getPanel_id() + " | " + buttons[j][k].getBtn_id() + " | " + buttons[j][k].getText() + '\n';
                        }
                    }
                }
                File saveFile = new File(filePath);
                try{
                    FileWriter fw = new FileWriter(saveFile);
                    BufferedWriter bw = new BufferedWriter(fw);

                    bw.write(textToWrite);

                    bw.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                setVisible(false);
            }
            startGame.setPause(!startGame.getIsPause());
            pausePopupMenu.setVisible(startGame.getIsPause());
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputSaveName.setText("");
                setVisible(false);
            }
        });

        inputSaveName.requestFocusInWindow();
    }
}