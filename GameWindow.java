import javax.swing.JFrame;

public class GameWindow extends JFrame{
    public GameWindow(){

        this.add(GamePanel.getInstance());
        this.setTitle("Poykemon");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}