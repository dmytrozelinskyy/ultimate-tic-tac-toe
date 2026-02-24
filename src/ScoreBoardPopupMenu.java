import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScoreBoardPopupMenu extends JWindow {
    /**Creating List Model (Default)*/
    private DefaultListModel listModel = new DefaultListModel();
    /**Creating List (JList)*/
    private JList list = new JList();
    /**Creating Scroll Pane (to ensure all our scores will be visible)*/
    private JScrollPane scrollableList;

    /**extractScoreFromString function -> extracting score from our file (line)*/
    private static int extractScoreFromString(String str) {
        int firstPosition = str.indexOf('|');
        if (firstPosition != -1) {
            int secondPosition = str.indexOf('|', firstPosition + 1);
            if (secondPosition != -1) {
                String scoreNumber = str.substring(secondPosition + 2);
                try {
                    return Integer.parseInt(scoreNumber);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number(result) format: " + scoreNumber);
                    return -1;
                }
            }
        }
        return -1;
    }
    /**Default Constructor (Creation of Score Board Menu)*/
    public ScoreBoardPopupMenu(){
        super();
        this.setSize(500,600);
        this.getRootPane()
            .setBorder(BorderFactory.createMatteBorder(3,3,3,3,Color.BLACK));

        setComponents();
        this.setVisible(false);
    }

    /**Setting List Model(Extracting scores and putting them into list (the biggest score goes first))*/
    private void setComponents(){
        int count = 1;                                                                                          // Start from one to find 1st, 2nd and 3rd places
        List<String> resultsContainer = new ArrayList<>();
        JButton exitButton = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/close.png"))));           // 'X' close button
        JLabel title = new JLabel("Score  Board");
        JPanel buttonPanel = new JPanel(new BorderLayout());

        try (BufferedReader file = new BufferedReader(new FileReader(new File("..\\UltimateTicTacToe\\results\\ScoreBoardResults.txt")))) {
            String line;
            while ((line = file.readLine()) != null)                                                            // Read line by line to the file end line
                resultsContainer.add(line);
        } catch (IOException e) {
            System.err.println("Unable to open file! Path provided: " + "ScoreBoardResults.txt");
        }
        resultsContainer.sort((left, right) -> {                                                                // Sorting taken results (bigger score firstly)
            int scoreL = extractScoreFromString(left);
            int scoreR = extractScoreFromString(right);
            return Integer.compare(scoreR, scoreL);
        });

        for(var element : resultsContainer){
            if(count == 1) listModel.addElement("1ˢᵗ --> " + element);
            else if(count == 2) listModel.addElement("2ⁿᵈ --> " + element);
            else if(count == 3) listModel.addElement("3ʳᵈ --> " + element);
            else listModel.addElement(element);
            count++;
        }
        list.setModel(listModel);
        scrollableList = new JScrollPane(list);

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

        this.add(scrollableList, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.NORTH);
    }

}
