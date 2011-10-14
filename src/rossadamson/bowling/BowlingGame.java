package rossadamson.bowling;

import rossadamson.bowling.Roll.InvalidRollException;

/**
 * Represents a bowling game.
 * It tracks the score and contains methods for rolling.
 * @author Ross Adamson
 */
public class BowlingGame {
    protected Roll currentRoll;
    protected Frame[] frames;
    protected int currentFrameIndex;
    public static final int NUMBER_OF_FRAMES = 10;
    public static final int NUMBER_OF_PINS = 10;
    
    /**
     * Constructor.
     */
    public BowlingGame() {
        init();
    }
    
    /**
     * Initialize this object.
     */
    public void init() {
        currentRoll = null;
        frames = new Frame[NUMBER_OF_FRAMES];
        for (int frameIndex = 0; frameIndex < NUMBER_OF_FRAMES; ++frameIndex) {
            frames[frameIndex] = new Frame();
        }
        frames[NUMBER_OF_FRAMES - 1].isLast = true;
        currentFrameIndex = 0;
    }
    
    /**
     * Get the total score of the game so far.
     * @return
     */
    public int totalScore() {
        int score = 0;
        
        for (int frameIndex = 0; frameIndex <= currentFrameIndex; ++frameIndex) {
            score += frames[frameIndex].getScore();
        }
        
        return score;
    }
    
    /**
     * Whether the game is finished.
     * @return
     */
    public boolean isFinished() {
        return lastFrame().isComplete();
    }
    
    /**
     * Get the last frame.
     * @return
     */
    protected Frame lastFrame() {
        return frames[NUMBER_OF_FRAMES - 1];
    }
    
    /**
     * Add a roll to the game.
     * @param pinsDown Number of pins down in the roll.
     * @throws GameFinishedException
     * @throws Roll.InvalidRollException
     */
    public void addRoll(int pinsDown) throws GameFinishedException, InvalidRollException {
        if (isFinished()) {
            throw new GameFinishedException();
        } else {
            if (currentRoll == null) {
                currentRoll = new Roll(pinsDown);
            } else {
                currentRoll.nextRoll = new Roll(pinsDown);
                currentRoll = currentRoll.nextRoll;
            }
        }
    }
    
    /**
     * Exception for adding a roll when the game is already over.
     * @author Ross Adamson
     */
    public class GameFinishedException extends Exception {
        /**
         * 
         */
        private static final long serialVersionUID = -7779553600270398528L;
    }
}
