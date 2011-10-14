package rossadamson.bowling;

import java.util.Collection;

/**
 * Represents a bowling game.
 * It tracks the score and contains methods for rolling.
 * @author Ross Adamson
 */
public class BowlingGame {
    protected Roll currentRoll;
    protected Frame[] frames;
    protected int currentFrameIndex;
    private int pinsUp;
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
        pinsUp = NUMBER_OF_PINS;
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
        return lastFrame().hasAllRolls();
    }
    
    /**
     * Get the last frame.
     * @return
     */
    protected Frame lastFrame() {
        return frames[NUMBER_OF_FRAMES - 1];
    }
    
    public Roll currentRoll() {
        return frames[currentFrameIndex].endRoll;
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
        } else if (pinsDown > pinsUp) {
            throw new InvalidRollException();
        } else {
            Roll roll = new Roll(pinsDown);
            
            // check for frame change
            if (frames[currentFrameIndex].hasAllRolls()) {
                // link the roll from the last frame to the new roll
                frames[currentFrameIndex].endRoll.nextRoll = roll;
                ++currentFrameIndex;
            }
            
            frames[currentFrameIndex].addRoll(roll);
        }
    }
    
    /**
     * Get the set of possible rolls.
     * @return Zero sized result means no more rolls are possible
     * because the game is over.
     * @throws InvalidRollException 
     */
    public Collection<Roll> possibleRolls() throws InvalidRollException {
        Frame frame = null;
        // check for complete frame
        if (frames[currentFrameIndex].hasAllRolls() && !frames[currentFrameIndex].isLast) {
            frame = frames[currentFrameIndex + 1];
        } else {
            frame = frames[currentFrameIndex];
        }
        
        return frame.possibleRolls();
    }
}
