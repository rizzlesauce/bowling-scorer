package rossadamson.bowling;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Ross Adamson
 */
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
        bowling.addRoll(new Roll(10));
        // spare
        bowling.addRoll(new Roll(3));
        bowling.addRoll(new Roll(7));
        // 4
        bowling.addRoll(new Roll(4));
        bowling.addRoll(new Roll(3));
        
        assertEquals(null, 20 + 14 + 4 + 3, bowling.totalScore());
        
        setUp();
        // perfect game
        for (int frameIndex = 0; frameIndex < BowlingGame.NUMBER_OF_FRAMES + 2; ++frameIndex) {
            bowling.addRoll(new Roll(10));
        }
        assertEquals("perfect game should have 300 score", 300, bowling.totalScore());
    }

    @Test
    public void testIsFinished() throws GameFinishedException, InvalidRollException {
        // perfect game
        for (int frameIndex = 0; frameIndex < BowlingGame.NUMBER_OF_FRAMES + 2; ++frameIndex) {
            bowling.addRoll(new Roll(10));
        }
        
        assertTrue("perfect game should be finished", bowling.isFinished());
    }
    
    @Test
    public void testNextRollFrame() throws GameFinishedException, InvalidRollException {
        assertEquals("next roll of empty game goes in first frame", bowling.frames[0], bowling.nextRollFrame());
        
        bowling.addRoll(new Roll(BowlingGame.ALL_PINS));
        assertEquals("next roll of after strike goes in second frame", bowling.frames[1], bowling.nextRollFrame());
        
        bowling.addRoll(new Roll(3));
        assertEquals("roll of 3 in second frame", bowling.frames[1], bowling.nextRollFrame());
        
        bowling.addRoll(new Roll(4));
        assertEquals("second roll in second frame", bowling.frames[2], bowling.nextRollFrame());
    }
    
    @Test
    public void testAddRoll() throws Exception {
        boolean thrown = false;
        
        try {
	        bowling.addRoll(new Roll(6));
        } catch (Exception e) {
            thrown = true;
        }
        assertFalse("no exception on add 6", thrown);
        
        thrown = false;
        try {
            bowling.addRoll(new Roll(5));
        } catch (InvalidRollException e) {
            thrown = true;
        } catch (Exception e) {
           throw e; 
        }
        assertTrue("roll with greater pins than left throws exception", thrown);
        
        // test GameFinishedException
        bowling = TestUtils.randomGame(BowlingGame.MAX_ROLLS, TestUtils.MAX_ERROR_ALLOWANCE);
        thrown = false;
        try {
            bowling.addRoll(new Roll(0));
        } catch (GameFinishedException e) {
            thrown = true;
        } catch (Exception e) {
            throw e;
        }
        assertTrue("add zero roll to complete game throws exception", thrown);
        
        bowling = TestUtils.randomGame(10, TestUtils.MAX_ERROR_ALLOWANCE);
        thrown = false;
        try {
            bowling.addRoll(new Roll(0));
        } catch (GameFinishedException e) {
            thrown = true;
        } catch (Exception e) {
            throw e;
        }
        assertFalse("add zero roll to incomplete game is fine", thrown);
    }
    
    @Test
    public void testToString() throws Exception {
        int numberOfGames = 5;
        
        // create complete and incomplete games
        for (int i = 0; i < numberOfGames; ++i) {
            // get rolls limits with high probability of producing a complete game
            int rollsLimit = TestUtils.random.nextInt(BowlingGame.MAX_ROLLS * 2);
            int errorAllowance = TestUtils.random.nextInt(TestUtils.MAX_ERROR_ALLOWANCE + 1);
            BowlingGame game = TestUtils.randomGame(rollsLimit, errorAllowance);
            System.out.println("rollsLimit:" + rollsLimit + "; errorAllowance:" + errorAllowance + 
                    "; " + (game.isFinished() ? "finished" : "unfinished"));
            System.out.println(game);
            System.out.println();
        }
    }
}
