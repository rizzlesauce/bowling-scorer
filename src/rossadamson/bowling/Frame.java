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
    /**
     * The first roll of the frame.
     */
    public Roll firstRoll;
    /**
     * The last roll of the frame.
     */
    public Roll lastRoll;
    /**
     * Whether this is the last frame of the game.
     * This gets set by the BowlingGame object that owns the frame.
     */
    public boolean isLast;
    
    /**
     * Constructor.
     */
    public Frame() {
        init();
    }

    /**
     * Initialize the object.
     */
    public void init() {
        firstRoll = lastRoll = null;
        isLast = false;
    }
    
    /**
     * Add a roll to the frame.
     * Precondition: Roll must not be null
     * Precondition: Outside checking must determine that roll is valid in the frame.
     */
    public void addRoll(Roll roll) {
        if (lastRoll == null) {
            firstRoll = lastRoll = roll;
        } else {
            lastRoll.nextRoll = roll;
            lastRoll = roll;
        }
    }
    
    /**
     * Get the number of rolls in this frame.
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
     */
    public boolean isStrike() {
        return firstRoll != null &&
                firstRoll.pins() == BowlingGame.ALL_PINS;
    }
    
    /**
     * Determine whether this is a spare frame.
     */
    public boolean isSpare() {
       return !isStrike() &&
               (rollCount() >= 2) &&
               (firstRoll.pins() + firstRoll.nextRoll.pins() == BowlingGame.ALL_PINS);
    }
    
    /**
     * Calculate the score for this frame.
     * If {@link #scoreIsComplete()} == false, this will return an incomplete
     * score.
     */
    public int getScore() {
        int score = 0;
        
        if (isStrike() || isSpare()) {
            // add up pins from 3 rolls, starting at the frame beginning
            score = Roll.pinsFromRollSequence(firstRoll, 3);
        } else {
            // add up pins from all the rolls in the frame
            score = Roll.pinsFromRollSequence(firstRoll, rollCount());
        }
       
        return score;
    }
    
    /**
     * Determine whether this frame's score is complete.
     * A frame is complete if all the rolls it depends on for a frame
     * score have been made.
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
     * frame.
     * If there are no rolls, return a full set of pins. Likewise,
     * if the last roll in the frame knocked down all the pins but the
     * frame isn't complete, return a full set of pins (this applies
     * only to the last frame of the game).
     */
    public int pinsUp() {
        int pinsLeft = BowlingGame.ALL_PINS;
        
        Iterator<Roll> rolls = rollIterator();
        while (rolls.hasNext()) {
            pinsLeft -= rolls.next().pins();
            if (isLast && (pinsLeft == 0) && !hasAllRolls()) {
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
     * Whether a roll can be added to this frame right now.
     * @param roll A hypothetical roll.
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
     */
    public Iterator<Roll> rollIterator() {
        return new RollIterator(firstRoll, lastRoll);
    }
}
