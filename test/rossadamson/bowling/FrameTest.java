package rossadamson.bowling;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import rossadamson.bowling.Roll.InvalidRollException;

public class FrameTest {
    static Frame frame;
    static Roll strike;
    static Roll spare1;
    static Roll spare2;
    static Roll under1;
    static Roll under2;
    static Roll zero;

    @Before
    public void setUp() throws Exception {
        frame = new Frame();
        
        strike = new Roll(BowlingGame.NUMBER_OF_PINS);
        
        Random random = new Random();
        
        // rolls that make a spare
        int firstSpare = random.nextInt(BowlingGame.NUMBER_OF_PINS);
        int secondSpare = BowlingGame.NUMBER_OF_PINS - firstSpare;
        spare1 = new Roll(firstSpare);
        spare2 = new Roll(secondSpare);
        
        // rolls that don't add up to 10
        int firstUnder = random.nextInt(BowlingGame.NUMBER_OF_PINS - 1); 
        int secondUnder = random.nextInt(BowlingGame.NUMBER_OF_PINS - firstUnder);
        under1 = new Roll(firstUnder);
        under2 = new Roll(secondUnder);
        
        zero = new Roll(0);
    }

    @Test
    public void testEmptyFrame() {
        assertEquals("score should be zero", 0, frame.getScore());
    }
    
    @Test
    public void testSingleStrike() {
        frame.addRoll(strike);
        assertEquals("single strike should get " + BowlingGame.NUMBER_OF_PINS + " points", BowlingGame.NUMBER_OF_PINS,
                frame.getScore());
    }
    
    @Test
    public void testStrikeAndRolls() {
        frame.addRoll(strike);
        strike.nextRoll = under1;
        under1.nextRoll = under2;
        assertEquals(null, BowlingGame.NUMBER_OF_PINS + under1.pins + under2.pins,
                frame.getScore());
    }
    
    @Test
    public void testLastFrame() throws Roll.InvalidRollException {
        frame.isLast = true;
        frame.addRoll(new Roll(strike.pins()));
        frame.addRoll(new Roll(strike.pins()));
        frame.addRoll(new Roll(strike.pins()));
        assertEquals(null, BowlingGame.NUMBER_OF_PINS * 3,
                frame.getScore());
    }
    
    public static Roll getRandomRoll() throws InvalidRollException {
        Random random = new Random();
        return new Roll(random.nextInt(BowlingGame.NUMBER_OF_PINS + 1));
    }
    
    @Test
    public void testSpare() throws Roll.InvalidRollException {
        Roll roll1 = new Roll(spare1.pins());
        Roll roll2 = new Roll(spare2.pins());
        Roll roll3 = getRandomRoll();
        frame.addRoll(roll1);
        frame.addRoll(roll2);
        roll2.nextRoll = roll3;
        assertEquals("", BowlingGame.NUMBER_OF_PINS + roll3.pins(), frame.getScore());
    }
    
    @Test
    public void testIsComplete() throws Exception {
        assertTrue("new frame should not be complete", !frame.isComplete());
        
        frame.addRoll(strike);
        assertTrue("strike with no additional roll should not be complete", !frame.isComplete());
        
        strike.nextRoll = spare1;
        assertTrue("strike with one additional roll should not be complete", !frame.isComplete());
        
        spare1.nextRoll = spare2;
        assertTrue("strike with two additional rolls should be complete", frame.isComplete());
        
        setUp();
        frame.addRoll(spare1);
        assertTrue("one roll should not be complete", !frame.isComplete());
        
        frame.addRoll(spare2);
        assertTrue("spare should not be complete", !frame.isComplete());
        
        spare2.nextRoll = under1;
        assertTrue("spare with nextRoll should be complete", frame.isComplete());
        
        setUp();
        frame.addRoll(under1);
        frame.addRoll(under2);
        assertTrue("two rolls should be complete", frame.isComplete());
    }

    @Test
    public void testRollIterator() {
        frame.isLast = true;
        Roll roll1 = spare1;
        Roll roll2 = spare2;
        Roll roll3 = strike;
        frame.addRoll(roll1);
        frame.addRoll(roll2);
        frame.addRoll(roll3);
        
        Iterator<Roll> iterator = frame.rollIterator();
        
        assertTrue("missing first element", iterator.hasNext());
        assertEquals("", roll1, iterator.next());
        assertTrue("missing second element", iterator.hasNext());
        assertEquals("", roll2, iterator.next());
        assertTrue("missing third element", iterator.hasNext());
        assertEquals("", roll3, iterator.next());        
    }
}
