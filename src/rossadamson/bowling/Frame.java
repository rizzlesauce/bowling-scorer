package rossadamson.bowling;

import java.util.Iterator;
import java.util.NoSuchElementException;

import rossadamson.bowling.Roll.InvalidRollException;

/**
 * A bowling frame.
 * A frame represents two tries to knock down all 10 pins.
 * Possibilities are a strike, a spare, two rolls that don't add up to 10. The
 * last frame acts differently than other frames.
 * @author Ross Adamson
 */
public class Frame {
    protected Roll startRoll;
    protected Roll endRoll;
    public boolean isLast;
    
    public Frame() {
        init();
    }

    public void init() {
        startRoll = endRoll = null;
        isLast = false;
    }
    
    /**
     * Add a roll to the frame.
     * Precondition: Roll must not be null
     * Precondition: Outside checking must determine that roll is valid in the frame.
     * @param roll 
     */
    public void addRoll(Roll roll) {
        if (endRoll == null) {
            startRoll = endRoll = roll;
        } else {
            endRoll.nextRoll = roll;
            endRoll = roll;
        }
    }

    /**
     * Calculate the score for this frame.
     * If isComplete() == false, this will return an incomplete
     * score.
     * @return
     * @throws InvalidRollException 
     */
    public int getScore() {
        int score = 0;
        
        if (isLast) {
            // add up the pins from the roles in the frame
            Iterator<Roll> rolls = rollIterator();
            while (rolls.hasNext()) {
                Roll currentRoll = rolls.next();
                score += currentRoll.pins();
                currentRoll = currentRoll.nextRoll;
            }
         } else {
             int pinsLeft = BowlingGame.NUMBER_OF_PINS;
            
            Iterator<Roll> rolls = rollIterator();
            while (rolls.hasNext()) {
                Roll currentRoll = rolls.next();
                if (currentRoll.isStrike()) {
                        // strike
                    // get the score of next two rolls
                    if (currentRoll.hasNext()) {
                        score += currentRoll.nextRoll.pins();
                        
                        if (currentRoll.nextRoll.hasNext()) {
                            score += currentRoll.nextRoll.nextRoll.pins();
                        }
                    }
                } else if (currentRoll.pins() == pinsLeft) {
                        // spare
                    // get the score of next roll
                    if (currentRoll.hasNext()) {
                        score += currentRoll.nextRoll.pins();
                    }
                }
                score += currentRoll.pins();
                pinsLeft -= currentRoll.pins();
                currentRoll = currentRoll.nextRoll;
            }
        }
        
        return score;
    }
    
    /**
     * Determine whether this frame is complete.
     * A frame is complete if all the rolls it depends on for a frame
     * score have been made.
     * @return
     */
    public boolean isComplete() {
        boolean complete = false;
        
        if (startRoll != null) {
            if (startRoll.isStrike()) {
                    // strike
                // check for next two rolls
                if (startRoll.hasNext() && startRoll.nextRoll.hasNext()) {
                    complete = true;
                }
            } else if (startRoll.hasNext()) {
                Roll second = startRoll.nextRoll;
                // check for spare
                if (startRoll.pins() + second.pins() == BowlingGame.NUMBER_OF_PINS) {
                        // spare
                    if (second.hasNext()) {
                        complete = true;
                    }
                } else {
                        // two rolls but not a spare
                    complete = true;
                }
            }
        }
        
        return complete;
    }
    
    /**
     * Get an iterator over the rolls in the frame.
     * @return
     */
    public Iterator<Roll> rollIterator() {
        return new RollIterator();
    }
        
    /**
     * An iterator for the rolls in this frame.
     * @author Ross Adamson
     */
    public class RollIterator implements Iterator<Roll> {
        public Roll currentRoll;
        
        public RollIterator() {
            currentRoll = startRoll;
        }
        
        @Override
        public boolean hasNext() {
            return (endRoll != null) && (currentRoll != endRoll.nextRoll);
        }

        @Override
        public Roll next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            
            Roll result = currentRoll;
            if (result != null) {
                currentRoll = result.nextRoll;
            }
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
