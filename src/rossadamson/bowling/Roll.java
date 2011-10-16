package rossadamson.bowling;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a bowling roll.
 * @author Ross Adamson
 */
public class Roll {
    public Roll nextRoll;
    private int pins;
   
    public Roll(int pins) {
        init(pins);
    }
    
    public void init(int pins) {
        setPins(pins);
        nextRoll = null;
    }
   
    /**
     * Set the number of pins down by this roll.
     * Precondition: pins >= 0
     * @param pins
     */
    private void setPins(int pins) {
        this.pins = pins;
    }
    
    /**
     * The number of pins knocked down by this roll.
     * @return
     */
    public int pins() {
        return this.pins;
    }
    
    /**
     * Whether there is a roll after this one.
     * @return
     */
    public boolean hasNext() {
        return nextRoll != null;
    }
    
    /**
     * Add up the pins from a sequence of rolls, starting at
     * the start roll.
     * If there aren't three rolls, add up as many as
     * possible. 
     * @param startRoll The first roll to start counting pins from
     * @param numberOfRolls The number of rolls to count pins from
     * @return
     */
    public static int pinsFromRollSequence(Roll startRoll, int numberOfRolls) {
        Roll roll = startRoll;
        int pinCount = 0;
        
        for (int rollCount = 0; (rollCount < numberOfRolls) && (roll != null); ++rollCount, roll = roll.nextRoll) {
            pinCount += roll.pins();
        }
        
        return pinCount;
    }
    
    /**
     * Whether a sequence of a certain size exists, starting from
     * the start roll.
     * @param startRoll The first roll to start counting
     * @param numberOfRolls The number of rolls to look for. Must be positive.
     * @return
     */
    public static boolean hasSequenceSize(Roll startRoll, int numberOfRolls) {
        int rollCount = 0;
        
        for (Roll roll = startRoll; (rollCount < numberOfRolls) && (roll != null); roll = roll.nextRoll) {
            ++rollCount;
        }
        
        return rollCount == numberOfRolls;
    }
     
    /**
     * Get a range of pin rolls, starting from 0 up to the specified maximum.
     * @param rolls
     * @param maxPins The greatest pin count in the range
     */
    public static Collection<Roll> getRangeOfRolls(int maxPins) {
        ArrayList<Roll> rolls = new ArrayList<Roll>();
        for (int pins = 0; pins <= maxPins; ++pins) {
            rolls.add(new Roll(pins));
        }
        return rolls;
    }
    
    /**
     * Whether another roll has the same pins as this one.
     * @param roll
     * @return
     */
    public boolean samePins(Roll roll) {
        return pins == roll.pins();
    }
}
