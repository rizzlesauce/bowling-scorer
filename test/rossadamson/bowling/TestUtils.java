package rossadamson.bowling;

import java.util.Random;

/**
 * Some methods to help with testing bowling classes.
 * @author Ross Adamson
 */
public class TestUtils {
    public static Random random = new Random();
    public static final int MAX_ERROR_ALLOWANCE = BowlingGame.ALL_PINS;
   
    /**
     * Get a random roll.
     * @return
     */
    public static Roll randomRoll() {
        return new Roll(random.nextInt(BowlingGame.ALL_PINS + 1));
    }
    
    /**
     * Get an array of random rolls.
     * @return
     */
    public static Roll[] randomRolls(int size) {
        Roll[] rolls = new Roll[size];
        for (int i = 0; i < size; ++i) {
            rolls[i] = randomRoll();
        }
        return rolls;
    }
    
    /**
     * Get a random game with a limited number of rolls.
     * @param rollsLimit The maximum number of rolls. A value of MAX_ROLLS will
     * ensure the game is completed.
     * @param errorAllowance The maximum amount any roll can be off. For example,
     * an allowance of 1 would only make it possible for any roll to be one off
     * from the highest possible roll. A value of MAX_ERROR_ALLOWANCE will ensure
     * that any valid roll is possible, regardless of how bad it is.
     * @return A game of bowling.
     * @throws InvalidRollException 
     * @throws GameFinishedException 
     */
    public static BowlingGame randomGame(int rollsLimit, int errorAllowance) throws GameFinishedException, InvalidRollException {
       BowlingGame game = new BowlingGame();
       
       for (int rollCount = 0; (rollCount < rollsLimit) && !game.isFinished(); ++rollCount) {
           // make a random, valid move
           Roll[] validRolls = game.possibleRolls().toArray(new Roll[0]);
           
           int minRollIndex = validRolls.length - 1 - errorAllowance;
           if (minRollIndex < 0) {
               minRollIndex = 0;
           }
           
           Roll roll = validRolls[random.nextInt(validRolls.length - minRollIndex) + minRollIndex];
           game.addRoll(roll);
       }
       
       return game;
    }
}
