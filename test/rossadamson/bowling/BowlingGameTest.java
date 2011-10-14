package rossadamson.bowling;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BowlingGameTest {
    public static BowlingGame bowling;

    @Before
    public void setUp() throws Exception {
        bowling = new BowlingGame();
    }

    @Test
    public void testInit() {
        bowling.init();
        assertEquals("init-ed bowling should have zero score", 0, bowling.totalScore());
    }

    @Test
    public void testTotalScore() throws Exception {
        // strike
        bowling.addRoll(10);
        // spare
        bowling.addRoll(3);
        bowling.addRoll(7);
        // 4
        bowling.addRoll(4);
        
        assertEquals(null, 20 + 14 + 4, bowling.totalScore());
        
        setUp();
        // perfect game
        for (int frameIndex = 0; frameIndex < BowlingGame.NUMBER_OF_FRAMES + 2; ++frameIndex) {
            bowling.addRoll(10);
        }
        assertEquals("perfect game should have 300 score", 300, bowling.totalScore());
    }

    @Test
    public void testIsFinished() throws GameFinishedException, InvalidRollException {
        // perfect game
        for (int frameIndex = 0; frameIndex < BowlingGame.NUMBER_OF_FRAMES + 2; ++frameIndex) {
            bowling.addRoll(10);
        }
            
        assertTrue("perfect game should be finished", bowling.isFinished());
    }
}
