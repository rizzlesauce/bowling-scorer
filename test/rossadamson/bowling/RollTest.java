package rossadamson.bowling;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

/**
 * @author Ross Adamson
 */
public class RollTest {
    
    @Test
    public void testInit() {
        Roll roll = new Roll(BowlingGame.ALL_PINS);
        roll.init(0);
        
        assertNull("nextRoll should be null", roll.nextRoll);
        assertEquals("pins should be zero", 0, roll.pins());
    }
    
    @Test
    public void testHasNext() {
        Roll roll = new Roll(BowlingGame.ALL_PINS);
        assertFalse("should not have next roll", roll.hasNext());
        
        Roll second = new Roll(BowlingGame.ALL_PINS);
        roll.nextRoll = second;
        assertTrue("should have next roll", roll.hasNext());
    }
    
    @Test
    public void testPins() {
       Roll roll;
       int timesToTest = 5;
       
       for (int i = 0; i < timesToTest; ++i) {
           int initValue = TestUtils.random.nextInt(BowlingGame.ALL_PINS + 1);
           roll = new Roll(initValue);
           assertEquals("roll has right number of pins", initValue, roll.pins());
       }
    }
    
    @Test
    public void testPinsFromRollSequence() {
        Roll roll1;
        Roll roll2;
        Roll roll3;
        int timesToTest = 10;
        
        assertEquals("null roll", 0, Roll.pinsFromRollSequence(null, 4));
        
        for (int i = 0; i < timesToTest; ++i) {
            roll1 = TestUtils.randomRoll();
            roll2 = TestUtils.randomRoll();
            roll3 = TestUtils.randomRoll();
            
            roll1.nextRoll = roll2;
            roll2.nextRoll = roll3;
        
            assertEquals("zero rolls", 0, Roll.pinsFromRollSequence(roll1, 0));
            assertEquals("first two rolls", roll1.pins() + roll2.pins(), 
                    Roll.pinsFromRollSequence(roll1, 2));
            assertEquals("three rolls", roll1.pins() + roll2.pins() + roll3.pins(),
                    Roll.pinsFromRollSequence(roll1, 3));
            assertEquals("four rolls when only 3 exist", roll1.pins() + roll2.pins() + roll3.pins(),
                    Roll.pinsFromRollSequence(roll1, 4));
        }
    }
    
    @Test
    public void testHasSequenceSize() {
        Roll roll1;
        Roll roll2;
       
        // null roll
        roll1 = null;
        int[] lengths = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (int i = 0; i < lengths.length; ++i) {
            assertFalse("null roll has size 0", Roll.hasSequenceSize(roll1, lengths[i]));
        }
         
        // one roll
        roll1 = TestUtils.randomRoll();
        assertTrue("one roll has size 1", Roll.hasSequenceSize(roll1, 1));
        
        roll2 = TestUtils.randomRoll();
        roll1.nextRoll = roll2;
        assertTrue("two rolls has size 2", Roll.hasSequenceSize(roll1, 2));
    }
    
    @Test
    public void testGetRangeOfRolls() {
        int[] maxValues = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (int i = 0; i < maxValues.length; ++i) {
            int maxValue = maxValues[i];
           
            Collection<Roll> rolls = Roll.getRangeOfRolls(maxValue);
            assertEquals("count number of rolls", maxValue + 1, rolls.size());
        }
    }
    
    @Test
    public void testSamePins() {
        Roll roll1 = null;
        Roll roll2 = null;
        int timesToTest = 5;
        
        for (int i = 0; i < timesToTest; ++i) {
            roll1 = TestUtils.randomRoll();
            roll2 = new Roll(roll1.pins());
            assertTrue("two rolls have same pin", roll1.samePins(roll2));
        }
    }
 }
