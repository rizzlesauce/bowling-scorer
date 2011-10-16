package rossadamson.bowling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A bowling frame.
 * A frame represents two tries to knock down all 10 pins.
 * Possibilities are a strike, a spare, two rolls that don't add up to 10. The
 * last frame acts differently than other frames.
 * @author Ross Adamson
 */
public class Frame {
    public Roll firstRoll;
    public Roll endRoll;
    public boolean isLast;
    
    public Frame() {
        init();
    }

    public void init() {
        firstRoll = endRoll = null;
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
            firstRoll = endRoll = roll;
        } else {
            endRoll.nextRoll = roll;
            endRoll = roll;
        }
    }
    
    /**
     * Get the number of rolls in this frame.
     * @return
     */
    public int rollCount() {
        int count = 0;
        Iterator<Roll> rolls = rollIterator();
        
        while (rolls.hasNext()) {
            ++count;
            rolls.next();
        }
        
        return count;
    }
    
    /**
     * Determine whether this is a strike frame.
     * @return
     */
    public boolean isStrike() {
        return firstRoll != null &&
                firstRoll.pins() == BowlingGame.ALL_PINS;
    }
    
    /**
     * Determine whether this is a spare frame.
     * @return
     */
    public boolean isSpare() {
       return !isStrike() &&
               (rollCount() >= 2) &&
               (firstRoll.pins() + firstRoll.nextRoll.pins() == BowlingGame.ALL_PINS);
    }
    
    /**
     * Calculate the score for this frame.
     * If isComplete() == false, this will return an incomplete
     * score.
     * @return
     */
    public int getScore() {
        int score = 0;
        
        if (isStrike() || isSpare()) {
            score = Roll.pinsFromRollSequence(firstRoll, 3);
        } else {
            score = Roll.pinsFromRollSequence(firstRoll, rollCount());
        }
       
        return score;
    }
    
    /**
     * Determine whether this frame's score is complete.
     * A frame is complete if all the rolls it depends on for a frame
     * score have been made.
     * @return
     */
    public boolean scoreIsComplete() {
        boolean complete = false;
        
        if (isStrike() || isSpare()) {
            complete = Roll.hasSequenceSize(firstRoll, 3);
        } else {
            complete = Roll.hasSequenceSize(firstRoll, 2);
        }
       
        return complete;
    }
    
    /**
     * Determine whether this frame has all the rolls it needs to be complete.
     * @return
     */
    public boolean hasAllRolls() {
        boolean complete = false;
        
        if (isLast) {
            if (isStrike() || isSpare()) {
                    // frame needs three rolls
                complete = (rollCount() == 3);
            } else {
                    // frame needs two rolls
                complete = (rollCount() == 2);
            }
        } else if (isStrike()) {
                // a strike frame only needs one roll
            complete = true;
        } else {
                // a frame that isn't a strike needs two rolls
            complete = (rollCount() == 2);
        }
        
        return complete;
    }
    
    /**
     * Get the number of pins left at the end of the last roll in the 
     * frame. If there are no rolls, return 10.
     * @return
     */
    public int pinsUp() {
        int pinsLeft = BowlingGame.ALL_PINS;
        
        Iterator<Roll> rolls = rollIterator();
        while (rolls.hasNext()) {
            pinsLeft -= rolls.next().pins();
            if (pinsLeft == 0) {
                pinsLeft = BowlingGame.ALL_PINS;
            }
        }
        
        return pinsLeft;
    }
    
    /**
     * Get the set of possible rolls.
     * @return Zero sized result means no more rolls are possible
     * because the game is over.
     */
    public Collection<Roll> possibleRolls() {
        Collection<Roll> possibleRolls = null;
        
        if (hasAllRolls()) {
            possibleRolls = new ArrayList<Roll>();
        } else {
            possibleRolls = Roll.getRangeOfRolls(pinsUp());
        }
 
        return possibleRolls;
    }
    
    /**
     * Whether a roll can be made in this frame right now.
     * @param roll
     * @return
     */
    public boolean canRoll(Roll roll) {
        Collection<Roll> possibleRolls = possibleRolls();
        boolean result = false;
        
        Iterator<Roll> rolls = possibleRolls.iterator();
        while (rolls.hasNext()) {
            if (roll.samePins(rolls.next())) {
               result = true; 
               break;
            }
        }
        
        return result;
    }
    
    /**
     * Get an iterator over the rolls in the frame.
     * @return
     */
    public Iterator<Roll> rollIterator() {
        return new RollIterator(firstRoll, endRoll);
    }
}
