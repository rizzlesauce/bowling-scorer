package rossadamson.bowling;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class RandomBowlRollGenerator implements Iterator<Roll> {
    private Random random;
    private BowlingGame bowlingGame;
    
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
        try {
            rolls = bowlingGame.possibleRolls().toArray(new Roll[0]);
        } catch (InvalidRollException e) {
            throw new NoSuchElementException();
        }
        
        Roll roll = rolls[random.nextInt(rolls.length)];
        
        return roll;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
