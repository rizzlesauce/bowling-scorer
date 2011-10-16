package rossadamson.bowling;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a bowling roll.
 * @author Ross Adamson
 */
public class Roll {
    /**
     * The roll that comes after this one. Null
     * if no more roles follow.
     */
    public Roll nextRoll;
    /**
     * The number of pins knocked down by the roll.
     */
    private int pins;
   
    /**
     * Constructor.
     * @param pins Number of pins knocked down by roll.
     */
    public Roll(int pins) {
        init(pins);
    }
    
    /**
     * Initialize the object.
     * @param pins Number of pins knocked down by roll.
     */
    public void init(int pins) {
        setPins(pins);
        nextRoll = null;
    }
   
    /**
     * Set the number of pins knocked down by this roll.
     * Precondition: pins >= 0
     */
    private void setPins(int pins) {
        this.pins = pins;
    }
    
    /**
     * The number of pins knocked down by this roll.
     */
    public int pins() {
        return this.pins;
    }
    
    /**
     * Whether there is a roll after this one.
     */
    public boolean hasNext() {
        return nextRoll != null;
    }
    
    /**
     * Add up the pins from a sequence of rolls, starting at
     * the start roll.
     * @param startRoll The first roll to start counting pins from.
     * @param maxRolls The maximum number of rolls to include
     * in the count. Must be positive.
     */
    public static int pinsFromRollSequence(Roll startRoll, int maxRolls) {
        Roll roll = startRoll;
        int pinCount = 0;
        
        for (int rollCount = 0; (rollCount < maxRolls) && (roll != null); ++rollCount, roll = roll.nextRoll) {
            pinCount += roll.pins();
        }
        
        return pinCount;
    }
    
    /**
     * Whether a sequence of a certain size exists, starting from
     * the start roll.
     * @param startRoll The first roll to start counting.
     * @param sequenceSize The number of rolls to look for. Must be positive.
     */
    public static boolean hasSequenceSize(Roll startRoll, int sequenceSize) {
        int rollCount = 0;
        
        for (Roll roll = startRoll; (rollCount < sequenceSize) && (roll != null); roll = roll.nextRoll) {
            ++rollCount;
        }
        
        return rollCount == sequenceSize;
    }
     
    /**
     * Get a range of pin rolls, starting from 0 up to the specified maximum.
     * @param maxPins The highest pin count in the range.
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
     * @param roll The other roll.
     */
    public boolean samePins(Roll roll) {
        return pins == roll.pins();
    }
}
