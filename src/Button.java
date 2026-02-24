import javax.swing.*;

public class Button extends JButton {
    private int btn_id;
    private int panel_id;

    /**Constructors*/
    public Button(int btn_id, int panel_id){
        super();

        this.btn_id = btn_id;
        this.panel_id = panel_id;

        this.setVisible(true);
    }

    /**Setters*/
    public void setBtn_id(int btn_id){
        this.btn_id = btn_id;
    }
    public void setPanel_id(int panel_id){ this.panel_id = panel_id; }


    /**Getters*/
    public int getBtn_id(){return btn_id;}
    public int getPanel_id(){return panel_id;}

    /**toString()*/
    @Override
    public String toString(){
        return "Button: btn_id: " + btn_id + "btn_panel_id: " + panel_id;
    }
}
