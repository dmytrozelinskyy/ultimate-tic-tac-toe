import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


/**This class is used to create a load popup menu (where we can choose the game we want to launch)*/
public class LoadPopupMenu extends JDialog {
    private static JComboBox<String> savings = new JComboBox<>();
    private JButton loadButton = new JButton("Load");
    private JButton cancelButton = new JButton("Cancel");
    private String filePath = "";
    private String textToWrite = "";
    private StartGame startGame;
    private MainMenu mainFrame;

    /**Default Constructor (Creation of Score Board Menu)*/
    public LoadPopupMenu(MainMenu frame_p, StartGame startGame){
        super(frame_p, "Load Game", ModalityType.TOOLKIT_MODAL);        // -- ModalityType.APPLICATION_MODAL here means, that the window cannot be resized or moved --

        this.startGame = startGame;
        this.mainFrame = frame_p;

        setComponents();
        this.setVisible(false);
    }
    public static java.util.List<String> listSaveFiles(String directory) {
        java.util.List<String> saveFiles = new ArrayList<>();
        Path dirPath = Paths.get(directory);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "save_*")) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    savings.addItem(entry.getFileName().toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return saveFiles;
    }
    public void setComponents(){
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonSectionPanel = new JPanel();
        JPanel inputSectionPanel = new JPanel();

        inputSectionPanel.add(new Label("Load Name: "));
        listSaveFiles("..\\UltimateTicTacToe\\saves");
        inputSectionPanel.add(savings);

        buttonSectionPanel.add(loadButton);
        buttonSectionPanel.add(cancelButton);

        mainPanel.add(inputSectionPanel, BorderLayout.CENTER);
        mainPanel.add(buttonSectionPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
        this.pack();                                                                                          // size compresses to fit all elements
        this.getRootPane()
                .setBorder(BorderFactory.createMatteBorder(3,3,3,3, Color.BLACK));

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = savings.getItemAt(savings.getSelectedIndex());  // replace with your file path
                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                    // Read the first line to get X_PLAY or O_PLAY
                    String firstLine = br.readLine();
                    String currentPlayer = firstLine.trim();
                    startGame.setPlayingSide(PlayingSide.valueOf(currentPlayer));
                    // Read the subsequent lines and process each one
                    String line, lastLine;
                    while ((line = br.readLine()) != null) {
                        lastLine = line;
                        String[] data = line.split("\\|");  // Split by the pipe character
                        if (data.length == 2) {
                            String panel_index = data[0].trim();
                            String panel_player = data[1].trim();

                            GamePanel[] panels = startGame.getPanels();
                            if(panel_player.equals("X")){
                                panels[Integer.parseInt(panel_index)].setBackgroundImage(new ImageIcon("..\\UltimateTicTacToe\\resources\\images\\letter-x.png").getImage());
                                startGame.repaint();
                            }
                            else if (panel_player.equals("O")){
                                panels[Integer.parseInt(panel_index)].setBackgroundImage(new ImageIcon("..\\UltimateTicTacToe\\resources\\images\\letter-o.png").getImage());
                                startGame.repaint();
                            }
                            else System.out.println("ERROR!");
                        } else if (data.length == 3) {
                            String panel_index = data[0].trim();
                            String btn_index = data[1].trim();
                            String button_text = data[2].trim();

                            Button[][] buttons = startGame.getPanels()[Integer.parseInt(panel_index)].getButtons();
                            switch(Integer.parseInt(btn_index)){
                                case 0:{
                                    buttons[0][0].setText(button_text);
                                    break;
                                }case 1:{
                                    buttons[0][1].setText(button_text);
                                    break;
                                }case 2:{
                                    buttons[0][2].setText(button_text);
                                    break;
                                }case 3:{
                                    buttons[1][0].setText(button_text);
                                    break;
                                }case 4:{
                                    buttons[1][1].setText(button_text);
                                    break;
                                }case 5:{
                                    buttons[1][2].setText(button_text);
                                    break;
                                }case 6:{
                                    buttons[2][0].setText(button_text);
                                    break;
                                }case 7:{
                                    buttons[2][1].setText(button_text);
                                    break;
                                }case 8:{
                                    buttons[2][2].setText(button_text);
                                    break;
                                }
                            }
                        }
                        if(lastLine != null){
                            String[] lastData = lastLine.split("\\|");
                            if (lastData.length == 3) {
                                String last_btn_index = data[0].trim();
                                String last_panel_index = data[1].trim();
                                String last_button_text = data[2].trim();

                                GamePanel[] panels = startGame.getPanels();
                                for(int i = 0; i < panels.length;++i){
                                    Button[][] buttons = panels[i].getButtons();
                                    for(int j = 0; j < startGame.getGridSize(); ++j){
                                        for (int k = 0; k <  startGame.getGridSize(); ++k){
                                            if(i == Integer.parseInt(last_panel_index)){
                                                buttons[j][k].setBorder(BorderFactory.createLineBorder(mainFrame.getSettingsPopupMenu().getChosenColor(), 4));
                                                if(buttons[j][k].getText().isEmpty()) buttons[j][k].setEnabled(true);
                                                continue;
                                            }
                                            buttons[j][k].setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
                                            buttons[j][k].setEnabled(false);
                                        }
                                    }
                                }

                            }
                        }
                    }
                    GamePanel[] panels = startGame.getPanels();
                    for (int i = 0; i < panels.length; ++i){
                        Button[][] buttons = panels[i].getButtons();
                        for(int j = 0; j < startGame.getGridSize(); ++j){
                            for (int k = 0; k < startGame.getGridSize(); ++k){
                                if(!buttons[j][k].getText().isEmpty()) buttons[j][k].setEnabled(false);
                            }
                        }
                    }
                } catch (IOException d) {
                    d.printStackTrace();
                }


                mainFrame.setContentPane(startGame);
                mainFrame.setVisible(true);
                setVisible(false);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

    }
}