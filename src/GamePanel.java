import javax.swing.*;
import java.awt.*;

/**This class is used to create a panels of our main game (small|little game boards)*/
public class GamePanel extends JPanel {
    private static final int GRID_SIZE = 3;
    private Button[][] buttons = new Button[GRID_SIZE][GRID_SIZE];
    private Image backgroundImage;

    // Constructor
    public GamePanel(int id){
        super();
        this.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        this.setBorder(BorderFactory.createLineBorder(new Color(0,28,62,255), 5));

        int count = 0;
        for(int i = 0; i < GRID_SIZE; ++i){
           for (int j = 0; j < GRID_SIZE; ++j){
               Button playButton = new Button(count, id);
               playButton.setFont(new Font("Arial", Font.PLAIN,20));
               playButton.setContentAreaFilled(false);
               playButton.setFocusPainted(false);
               playButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));        // -- default button borders (we can change color in settings [for available buttons]) --
               playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
               buttons[i][j] = playButton;
               this.add(playButton);
               count++;
           }
        }
        this.setVisible(true);
    }

    // Setters
    public void setBackgroundImage(Image backgroundImage){this.backgroundImage = backgroundImage;}

    // Getters
    public Button[][] getButtons(){return buttons;}

    // Drawing Image As Background
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
