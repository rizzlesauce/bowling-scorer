package rossadamson.bowling;

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
     * Determine whether this frame's score is complete.
     * A frame is complete if all the rolls it depends on for a frame
     * score have been made.
     * @return
     */
    public boolean scoreIsComplete() {
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
     * Determine whether this frame has all the rolls it needs to be complete.
     * @return
     */
    public boolean hasAllRolls() {
        boolean complete = false;
        
        Iterator<Roll> rolls = rollIterator();
        
        if (rolls.hasNext()) {
                // at least one roll
            Roll start = rolls.next();
            if (start.isStrike()) {
                    // strike
                if (isLast) {
	                // check for two more rolls
	                if (start.hasNext() && start.nextRoll.hasNext()) {
	                    complete = true;
	                }
                } else {
                        // a strike completes the frame
                    complete = true;
                }
            } else if (rolls.hasNext()) {
                    // at least two rolls
                if (isLast) {
	                // check for spare
	                Roll second = start.nextRoll;
	                if (start.pins() + second.pins() == BowlingGame.NUMBER_OF_PINS) {
	                        // spare
	                    // check for third roll
	                    if (second.hasNext()) {
	                        complete = true;
	                    }
	                } else {
	                        // two rolls but no spare
	                    complete = true;
	                }
                } else {
                        // two rolls completes the frame
                    complete = true;
                }
            }
        }
        
        return complete;
    }
    
    /**
     * Get the set of possible rolls.
     * @return Zero sized result means no more rolls are possible
     * because the game is over.
     * @throws InvalidRollException 
     */
    public Collection<Roll> possibleRolls() throws InvalidRollException {
        Collection<Roll> possibleRolls = null;

        Iterator<Roll> rolls = rollIterator();
        
        if (rolls.hasNext()) {
                // at least one roll
            Roll start = rolls.next();
            if (start.isStrike()) {
                    // strike
                if (isLast) {
	                // need two more rolls
	                if (start.hasNext()) {
	                    Roll second = start.nextRoll;
	                    
	                    if (!second.hasNext()) {
	                        possibleRolls = Roll.getRangeOfRolls(0, 10);
	                    }
	                } else {
	                    possibleRolls = Roll.getRangeOfRolls(0, 10);
	                }
                }
            } else if (rolls.hasNext()) {
                    // at least two rolls
                if (isLast) {
	                // check for spare
	                Roll second = start.nextRoll;
	                if (start.pins() + second.pins() == BowlingGame.NUMBER_OF_PINS) {
	                        // spare
	                    // check for third roll
	                    if (!second.hasNext()) {
	                        possibleRolls = Roll.getRangeOfRolls(0, 10);
	                    }
	                }
                }
            } else {
                
            }
        }
  
        return possibleRolls;
    }
    
    /**
     * Get an iterator over the rolls in the frame.
     * @return
     */
    public Iterator<Roll> rollIterator() {
        return new RollIterator(startRoll, endRoll);
    }
}
