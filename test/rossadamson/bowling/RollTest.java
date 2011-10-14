package rossadamson.bowling;

import static org.junit.Assert.*;

import org.junit.Test;

import rossadamson.bowling.Roll.InvalidRollException;

public class RollTest {

    @Test(expected=Roll.InvalidRollException.class)
    public void testRollTooBig() throws InvalidRollException {
        @SuppressWarnings("unused")
        Roll roll = new Roll(BowlingGame.NUMBER_OF_PINS + 1);
    }
    
    @Test(expected=Roll.InvalidRollException.class)
    public void testRollTooLittle() throws InvalidRollException {
        @SuppressWarnings("unused")
        Roll roll = new Roll(-1);
    }

    @Test
    public void testInit() throws InvalidRollException {
        Roll roll = new Roll(BowlingGame.NUMBER_OF_PINS);
        roll.init(0);
        
        assertNull("nextRoll should be null", roll.nextRoll);
        assertEquals("pins should be zero", 0, roll.pins);
    }
    
    @Test
    public void testHasNext1() throws InvalidRollException {
        Roll roll = new Roll(BowlingGame.NUMBER_OF_PINS);
        assertTrue("should not have next roll", !roll.hasNext());
    }
    
    @Test
    public void testHasNext2() throws InvalidRollException {
        Roll roll = new Roll(BowlingGame.NUMBER_OF_PINS);
        Roll second = new Roll(BowlingGame.NUMBER_OF_PINS);
        roll.nextRoll = second;
        assertTrue("should have next roll", roll.hasNext());
    }
    
    @Test
    public void testIsStrike() throws InvalidRollException {
        Roll roll = new Roll(BowlingGame.NUMBER_OF_PINS);
        assertTrue("roll should be strike", roll.isStrike());
    }

}
