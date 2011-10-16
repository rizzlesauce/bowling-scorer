package rossadamson.bowling;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Ross Adamson
 */
public class FrameTest {
    static Frame frame;
    static Frame strikeFrame;
    static Roll strike;
    static Roll spare1;
    static Roll spare2;
    static Roll under1;
    static Roll under2;
    static Roll zero;
    private Frame frameWithOneRoll;
    private Frame twoRollFrame;
    private Frame spareFrame;
    private Frame twoZerosFrame;
   
    @Before
    public void setUp() throws Exception {
        frame = new Frame();
        
        strike = new Roll(BowlingGame.ALL_PINS);
        
        // rolls that make a spare
        int firstSpare = TestUtils.random.nextInt(BowlingGame.ALL_PINS);
        int secondSpare = BowlingGame.ALL_PINS - firstSpare;
        spare1 = new Roll(firstSpare);
        spare2 = new Roll(secondSpare);
        
        // rolls that don't add up to 10
        int firstUnder = TestUtils.random.nextInt(BowlingGame.ALL_PINS - 2) + 1;
        int secondUnder = TestUtils.random.nextInt(BowlingGame.ALL_PINS - firstUnder);
        under1 = new Roll(firstUnder);
        under2 = new Roll(secondUnder);
        
        zero = new Roll(0);
        
        strikeFrame = new Frame();
        strikeFrame.addRoll(new Roll(strike.pins()));
        
        frameWithOneRoll = new Frame();
        frameWithOneRoll.addRoll(new Roll(under1.pins()));
        
        twoRollFrame = new Frame();
        twoRollFrame.addRoll(new Roll(under1.pins()));
        twoRollFrame.addRoll(new Roll(under2.pins()));
        
        spareFrame = new Frame();
        spareFrame.addRoll(new Roll(spare1.pins()));
        spareFrame.addRoll(new Roll(spare2.pins()));
        
        twoZerosFrame = new Frame();
        twoZerosFrame.addRoll(new Roll(0));
        twoZerosFrame.addRoll(new Roll(0));
    }
    
    @Test
    public void testAddRoll() {
        int[] numbersOfRolls = {1, 2, 3};
        for (int i = 0; i < numbersOfRolls.length; ++i) {
            
            Roll[] rolls = TestUtils.randomRolls(numbersOfRolls[i]);
            frame.init();
            
            for (int j = 0; j < rolls.length; ++j) {
                frame.addRoll(rolls[j]);
            }
            
            assertEquals("first roll matches", rolls[0], frame.firstRoll);
            assertEquals("last roll matches", rolls[rolls.length - 1], frame.endRoll);
        }
    }
    
    @Test
    public void testRollCount() {
        int[] numbersOfRolls = {0, 1, 2, 3};
        for (int i = 0; i < numbersOfRolls.length; ++i) {
            Roll[] rolls = TestUtils.randomRolls(numbersOfRolls[i]);
            frame.init();
            
            for (int j = 0; j < rolls.length; ++j) {
                frame.addRoll(rolls[j]);
            }
            
            assertEquals("number of rolls", rolls.length, frame.rollCount());
        }
    }
    
    @Test
    public void testIsStrike() {
        assertTrue("strike frame is strike frame", strikeFrame.isStrike());
        assertFalse("spare frame is not strike", spareFrame.isStrike());
        assertFalse("empty frame is not strike frame", frame.isStrike());
        assertFalse("one roll frame is not strike", frameWithOneRoll.isStrike());
        assertFalse("two roll frame is not strike", twoRollFrame.isStrike());
        assertFalse("two zeros frame is not strike", twoZerosFrame.isStrike());
    }
    
    @Test
    public void testIsSpare() {
        assertTrue("spare rolls is spare frame", spareFrame.isSpare());
        assertFalse("strike frame is not spare frame", strikeFrame.isSpare());
        assertFalse("empty frame is not spare frame", frame.isSpare());
        assertFalse("one roll frame is not spare", frameWithOneRoll.isSpare());
        assertFalse("two roll frame is not spare", twoRollFrame.isSpare());
        assertFalse("two zeros frame is not spare", twoZerosFrame.isSpare());
    }
    
    @Test
    public void testGetScore() {
        assertEquals("score should be zero", 0, frame.getScore());
        
        frame.addRoll(strike);
        assertEquals("single strike should get " + BowlingGame.ALL_PINS + " points", BowlingGame.ALL_PINS,
                frame.getScore());
        
        frame.init();
        frame.addRoll(strike);
        strike.nextRoll = under1;
        under1.nextRoll = under2;
        assertEquals("strike and two rolls", BowlingGame.ALL_PINS + under1.pins() + under2.pins(),
                frame.getScore());
        
        Frame lastFrame = new Frame();
        lastFrame.isLast = true;
        Roll roll1 = new Roll(6);
        Roll roll2 = new Roll(4);
        Roll roll3 = new Roll(5);
        lastFrame.addRoll(roll1);
        lastFrame.addRoll(roll2);
        lastFrame.addRoll(roll3);
        assertEquals("last frame with spare", roll1.pins() + roll2.pins() + roll3.pins(),
                lastFrame.getScore());
        
        frame.init();
        frame.isLast = true;
        frame.addRoll(new Roll(strike.pins()));
        frame.addRoll(new Roll(strike.pins()));
        frame.addRoll(new Roll(strike.pins()));
        assertEquals("last frame with three perfect rolls", BowlingGame.ALL_PINS * 3,
                frame.getScore());
        
        roll1 = new Roll(spare1.pins());
        roll2 = new Roll(spare2.pins());
        roll3 = TestUtils.randomRoll();
        frame.init();
        frame.addRoll(roll1);
        frame.addRoll(roll2);
        roll2.nextRoll = roll3;
        assertEquals("spare frame", BowlingGame.ALL_PINS + roll3.pins(), frame.getScore());
    }
   
    @Test
    public void testScoreIsComplete() throws Exception {
        assertTrue("new frame should not be complete", !frame.scoreIsComplete());
        
        frame.addRoll(strike);
        assertTrue("strike with no additional roll should not be complete", !frame.scoreIsComplete());
        
        strike.nextRoll = spare1;
        assertTrue("strike with one additional roll should not be complete", !frame.scoreIsComplete());
        
        spare1.nextRoll = spare2;
        assertTrue("strike with two additional rolls should be complete", frame.scoreIsComplete());
        
        
        setUp();
        frame.addRoll(spare1);
        assertTrue("one roll should not be complete", !frame.scoreIsComplete());
        
        frame.addRoll(spare2);
        assertTrue("spare should not be complete", !frame.scoreIsComplete());
        
        spare2.nextRoll = under1;
        assertTrue("spare with nextRoll should be complete", frame.scoreIsComplete());
        
        setUp();
        frame.addRoll(under1);
        frame.addRoll(under2);
        assertTrue("two rolls should be complete", frame.scoreIsComplete());
        
        Frame lastFrame = new Frame();
        lastFrame.isLast = true;
        lastFrame.addRoll(new Roll(4));
        lastFrame.addRoll(new Roll(2));
        assertTrue("last frame with two rolls is complete", lastFrame.scoreIsComplete());
    }
    
    @Test
    public void testHasAllRolls() {
        assertFalse("empty frame doesn't have all rolls", frame.hasAllRolls()); 
        assertTrue("frame with strike has all rolls", strikeFrame.hasAllRolls());
        assertFalse("frame with one non-strike roll doesn't have all rolls", frameWithOneRoll.hasAllRolls());
        assertTrue("frame with two rolls is complete", twoRollFrame.hasAllRolls());
        assertTrue("spare frame is complete", spareFrame.hasAllRolls());
        assertTrue("two zeros is complete", twoZerosFrame.hasAllRolls());
        
        Frame lastFrame = new Frame();
        lastFrame.isLast = true;
        lastFrame.addRoll(new Roll(10));
        lastFrame.addRoll(new Roll(10));
        assertFalse("two strikes in last frame not complete", lastFrame.hasAllRolls());
        
        lastFrame.addRoll(new Roll(10));
        assertTrue("three strikes in last frame complete", lastFrame.hasAllRolls());
        
        lastFrame.init();
        lastFrame.isLast = true;
        lastFrame.addRoll(new Roll(3));
        lastFrame.addRoll(new Roll(7));
        assertFalse("spare in last frame not complete", lastFrame.hasAllRolls());
        
        lastFrame.addRoll(new Roll(2));
        assertTrue("spare with extra in last frame is complete", lastFrame.hasAllRolls());
        
        lastFrame.init();
        lastFrame.addRoll(new Roll(4));
        lastFrame.addRoll(new Roll(3));
        assertTrue("last frame with two rolls is complete", lastFrame.hasAllRolls());
    }

    
    @Test
    public void testPinsUp() {
        assertEquals("empty frame has 10 pins", 10, frame.pinsUp());
        spareFrame.isLast = true;
        assertEquals("spare last frame has 10 pins left", 10, spareFrame.pinsUp());
        strikeFrame.isLast = true;
        assertEquals("strike last frame has 10 pins left", 10, strikeFrame.pinsUp());
        assertEquals("one roll", BowlingGame.ALL_PINS - frameWithOneRoll.firstRoll.pins(), frameWithOneRoll.pinsUp());
        assertEquals("two rolls", BowlingGame.ALL_PINS - twoRollFrame.firstRoll.pins() - twoRollFrame.endRoll.pins(),
                twoRollFrame.pinsUp());
    }

    @Test
    public void testPossibleRolls() {
        int pins = 2;
        frame.addRoll(new Roll(pins));
        assertEquals("one roll", BowlingGame.ALL_PINS - pins + 1, frame.possibleRolls().size());
    }
    
    @Test
    public void testCanRoll() {
       assertTrue("valid second roll", frameWithOneRoll.canRoll(
               new Roll(BowlingGame.ALL_PINS - under1.pins())));
       assertFalse("invalid second roll", frameWithOneRoll.canRoll(
               new Roll(BowlingGame.ALL_PINS - under1.pins() + 1)));
       assertTrue("strike on first roll", frame.canRoll(new Roll(BowlingGame.ALL_PINS)));
       
       frame.init();
       frame.isLast = true;
       frame.addRoll(new Roll(BowlingGame.ALL_PINS));
       frame.addRoll(new Roll(BowlingGame.ALL_PINS));
       assertTrue("two strikes on last roll", frame.canRoll(
               new Roll(BowlingGame.ALL_PINS)));
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
