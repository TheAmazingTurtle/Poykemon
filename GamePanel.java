import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener{
    public static final int SRCEEN_WIDTH = 480;
    public static final int SRCEEN_HEIGHT = 480;
    public static final int TILE_SIZE = 32;
    public static final int FPS = 60; 

    private final GameState gameState;
    private final Player player;
    private final Timer timer;
    private final HashSet<Integer> heldMovementKeys;
    private int lastMovementKey;

    public static GamePanel gamePanel;

    public GamePanel(){
        this.setPreferredSize(new Dimension(SRCEEN_WIDTH, SRCEEN_HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(this);

        this.gameState = GameState.getInstance();
        this.player = Player.getInstance();
        this.heldMovementKeys = new HashSet<>();
        this.lastMovementKey = KeyEvent.VK_UNDEFINED;
        
        int delay = 1000 / FPS; 
        this.timer = new Timer(delay, this);
        this.timer.start();
    }

    public static GamePanel getInstance(){
        if (gamePanel == null){
            gamePanel = new GamePanel();
        }
        return gamePanel;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        gameState.getScreenState().drawScreenState(g);

        gameState.drawOptionNavigator(g);

        if (gameState.getDialogueBox() != null){
            gameState.getDialogueBox().drawDialogueBox(g);
        }
    }

    @Override
	public void actionPerformed(ActionEvent e) {
        processHeldMovementInput();
        gameState.getScreenState().updateScreenState();
        repaint();
	}

    private void processHeldMovementInput(){
        if (gameState.getDialogueBox() != null || !gameState.getOptionNavStack().isEmpty() || player.getDestination() != null){
            return;
        }

        if (heldMovementKeys.contains(lastMovementKey)){
            player.handleMovementKey(lastMovementKey);
            return;
        }

        if (!heldMovementKeys.isEmpty()){
            int keyCode = heldMovementKeys.iterator().next();
            lastMovementKey = keyCode;
            player.handleMovementKey(keyCode);
        }
    }

    private boolean isMovementKey(int keyCode){
        return switch(keyCode){
            case KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_S, KeyEvent.VK_DOWN,
                 KeyEvent.VK_A, KeyEvent.VK_LEFT, KeyEvent.VK_D, KeyEvent.VK_RIGHT -> true;
            default -> false;
        };
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameState.getDialogueBox() != null) {
            gameState.handleDialogueBoxInput(e);
        }
        else if (!gameState.getOptionNavStack().isEmpty()){
            gameState.getActiveOptionNavigator().handleKeyboardInput(e);
        }
        else {
            if (isMovementKey(e.getKeyCode())){
                heldMovementKeys.add(e.getKeyCode());
                lastMovementKey = e.getKeyCode();
            }

            player.handleMovementInput(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        heldMovementKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // pass
    }
}
