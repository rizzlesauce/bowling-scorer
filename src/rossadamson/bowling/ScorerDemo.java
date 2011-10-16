package rossadamson.bowling;

import java.util.Scanner;

/**
 * A bowling game to be played from the command line.
 * Input are rolls. Output is the score board.
 * @author Ross Adamson
 */
public class ScorerDemo {
    public static final boolean defaultShowScore = true;
    public static final boolean defaultShowPossibleRolls = true;
    public static final boolean defaultShowPinsLeft = true;

    /**
     * Let the user put in rolls interactively to a bowling game.
     * @param args
     * @throws InvalidRollException 
     * @throws GameFinishedException 
     */
    public static void main(String[] args) throws GameFinishedException, InvalidRollException {
        System.out.println("Ready to bowl?! Good luck! Type \"exit\" to quit at any time," +
                " \"reset\" to start over.");
        
        BowlingGame game = new BowlingGame();
        
        Scanner in = new Scanner(System.in);
        
        boolean showScore = defaultShowScore;
        boolean showPossibleRolls = defaultShowPossibleRolls;
        boolean showPinsLeft = defaultShowPinsLeft;
        boolean done = false;
        
        while (!done) {
            int pinsLeft = game.nextRollFrame().pinsUp();
                
            System.out.println();
            
            if (showScore) {
	            // show current score
	            System.out.println(game);
            }
            
            if (showPinsLeft) {
                // show how many pins are left
	            System.out.println("" + pinsLeft + " pins left."); 
            }
            
            if (showScore || showPinsLeft) {
                System.out.println();
            }
           
            if (showPossibleRolls) {
	            // list the possible rolls
	            Roll[] rolls = game.possibleRolls().toArray(new Roll[0]);
	            
	            System.out.print("(Possible rolls: ");
	            for (int i = 0; i < rolls.length; ++i) {
	                if (i != 0) {
	                    System.out.print(", ");
	                }
	                System.out.print(rolls[i].pins());
	            }
	            System.out.print(")");
	            System.out.println();
            }
            
            // reset show flags
            showScore = defaultShowScore;
            showPinsLeft = defaultShowPinsLeft;
            showPossibleRolls = defaultShowPossibleRolls;
	                
            System.out.print("Roll? ");
                
            boolean keepGoing = true;
            int pins = 0;
            try {
                // get input
                String input = in.nextLine();
                if (input.toLowerCase().contains("exit") || input.toLowerCase().contains("quit")) {
                    System.out.println("Quiting game");
                    return;
                } else if (input.toLowerCase().contains("restart") || input.toLowerCase().contains("reset")) {
                    System.out.println("Restarting game");
                    game.init();
                    keepGoing = false;
                } else {
	                pins = Integer.parseInt(input);
                }
            } catch (Exception e) {
                System.out.print("Error reading input. Please try again.");
                showScore = false;
                showPinsLeft = false;
                showPossibleRolls = false;
                keepGoing = false;
            }
            
            if (keepGoing) {
	            // make the roll
	            Roll roll = new Roll(pins);
	            if (game.canRoll(roll)) {
	                
	                game.addRoll(roll);
	                
	                int pinsMissed = pinsLeft - roll.pins();
	                
	                System.out.print("You knocked down " + roll.pins() + " pins!");
	                if (roll.pins() == BowlingGame.ALL_PINS) {
	                    System.out.print(" Perfect shot!");
	                } else if (pinsMissed == 0) {
	                    System.out.print(" You got all of them!");
	                } else if (pinsMissed == 1) {
	                    System.out.print(" Good job!");
	                } else if (pinsMissed == 2) {
	                    System.out.print(" Not bad!");
	                }
	                System.out.println();
	                
	                // check for game over
		            if (game.isFinished()) {
				        System.out.println();
				        
				        System.out.print("You finished!");
				        
				        if (game.totalScore() == 300) {
				            System.out.print(" Wow! A perfect game!");
				        } else if (game.totalScore() > 250) {
				            System.out.print(" Very impressive!");
				        } else if (game.totalScore() > 200) {
				            System.out.print(" Way to go!");
				        } else if (game.totalScore() > 150) {
				            System.out.print(" Not bad!");
				        }
				        System.out.println();
				        System.out.println();
				        System.out.println(game);
				        
				        done = true;
		            }
	            } else {
	                System.out.print("Invalid roll. Please try again.");
	                showScore = false;
	                showPossibleRolls = false;
	                showPinsLeft = false;
	            }
            }
        }
    }
}
