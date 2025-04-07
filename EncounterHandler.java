

public class EncounterHandler {
    private static final int[] rockEffectiveness = {10,60};

    private final Poykemon poykemon;
    private GameState gameState = GameState.getInstance();
    private int tolerance;
    private float currentCaptureChance;

    public EncounterHandler(Poykemon poykemon){
        this.poykemon = poykemon;
        this.tolerance = 100;
        this.currentCaptureChance = Randomizer.generateRandomValue(poykemon.getCaptureChanceRange());
    }

    public void throwBall(Poykeball ball){
        ball.useItem();

        if (Randomizer.isSuccessful(currentCaptureChance + ball.getCapturePower())){
            Player player = Player.getInstance();

            gameState.setDialogueBox(new DialogueBox("System", new String[]{poykemon.getName() + " was captured successfully."}));
            System.out.println("Poykemon successfully captured.");

            player.addPoykemonToCollection(poykemon);
            player.getLocation().incrementNumPoykemonCaught();
            ProgressManager.getInstance().checkProgression(ProgressManager.ACTION_CATCH + player.getLocation() + player.getLocation().getNumPoykemonCaught());

            gameState.exitEncounter();
            return;
        }

        gameState.setDialogueBox(new DialogueBox("System", new String[]{"Capture Failed."}));
        System.out.println("Capture Failed\nChance: " + currentCaptureChance + "   Tolerance: " + tolerance);

        this.tolerance -= 10;
        if (tolerance <= 0){
            gameState.exitEncounter();
        }
    }

    public void throwBait(Bait bait){
        bait.useItem();

        String[] verb = {"savoured", "ate", "gobbled", "swallowed", "adored", "loved", "really really loved"};

        gameState.setDialogueBox(new DialogueBox("System", new String[]{poykemon.getName() + " " + verb[Randomizer.generateRandomValue(new int[]{0, verb.length})] +  " the " + bait.getName() + "."}));

        currentCaptureChance += bait.getBaitPower();
    }

    public void throwRock(){
        int effectiveness = Randomizer.generateRandomValue(rockEffectiveness);

        tolerance -= effectiveness;
        currentCaptureChance += effectiveness/2;

        String rockMessage;
        
        if (effectiveness < 20)
            rockMessage = " was only grazed by the rock.";
        else if (effectiveness < 40)
            rockMessage = " took a hard blow.";
        else
            rockMessage = " got hurt greatly.";
        gameState.setDialogueBox(new DialogueBox("System", new String[]{poykemon.getName() + rockMessage}));


        System.out.println("Chance: " + currentCaptureChance + "   Tolerance: " + tolerance);
        checkTolerance();   
    }

    public void checkTolerance(){
        if (tolerance <= 0){
            gameState.setDialogueBox(new DialogueBox("System", new String[]{poykemon.getName() + " fled."}));
            gameState.exitEncounter();
        }
    }
}
