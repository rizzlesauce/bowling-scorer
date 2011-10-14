package rossadamson.bowling;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The roll of a bowling ball and the pins it knocked down.
 * @author Ross Adamson
 */
public class Roll {
    public Roll nextRoll;
    protected int pins;
    
    public Roll(int pins) throws InvalidRollException {
        init(pins);
    }
    
    protected void init(int pins) throws InvalidRollException {
        setPins(pins);
        nextRoll = null;
    }
    
    /**
     * Set the number of pins down by this roll.
     * @param pins
     * @throws InvalidRollException
     */
    protected void setPins(int pins) throws InvalidRollException {
        if (pins > BowlingGame.NUMBER_OF_PINS || pins < 0) {
            throw new InvalidRollException();
        }
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
     * Whether this roll is a strike.
     * @return
     */
    public boolean isStrike() {
        return pins == BowlingGame.NUMBER_OF_PINS;
    }
    
    /**
     * Get a range of pin rolls.
     * @param rolls
     * @param minPins The smallest pin
     * @param maxPins The greatest pin
     * @throws InvalidRollException
     */
    public static Collection<Roll> getRangeOfRolls(int minPins, int maxPins) throws InvalidRollException {
        ArrayList<Roll> rolls = new ArrayList<Roll>();
        for (int pins = minPins; pins <= maxPins; ++pins) {
            rolls.add(new Roll(pins));
        }
        return rolls;
    }
}
