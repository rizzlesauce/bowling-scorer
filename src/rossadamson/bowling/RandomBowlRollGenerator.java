package rossadamson.bowling;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Generates random, valid rolls for a bowling game.
 * @author Ross Adamson
 */
public class RandomBowlRollGenerator implements Iterator<Roll> {
    /**
     * Random generator.
     */
    private Random random;
    /**
     * The bowling game to add rolls to.
     */
    private BowlingGame bowlingGame;
    
    /**
     * Constructor.
     * @param bowlingGame The game to add rolls to.
     */
    public RandomBowlRollGenerator(BowlingGame bowlingGame) {
        random = new Random();
        this.bowlingGame = bowlingGame;
    }

    @Override
    public boolean hasNext() {
        return !bowlingGame.isFinished();
    }

    @Override
    public Roll next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
         
        // get a random roll that works
        Roll[] rolls = null;
        rolls = bowlingGame.possibleRolls().toArray(new Roll[0]);
        
        Roll roll = rolls[random.nextInt(rolls.length)];
        
        return roll;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
