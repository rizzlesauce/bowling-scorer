package rossadamson.bowling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a bowling game.
 * It tracks the score and contains methods for rolling.
 * @author Ross Adamson
 */
public class BowlingGame {
    /**
     * The 10 frames of the game.
     */
    public Frame[] frames;
    /**
     * The index to the frame where the last roll was
     * made, or 0 if no rolls have been made.
     */
    public int currentFrameIndex;
    /**
     * Number of frames in a bowling game.
     */
    public static final int NUMBER_OF_FRAMES = 10;
    /**
     * Number of pins at the start of each frame.
     */
    public static final int ALL_PINS = 10;
    /**
     * Maximum number of rolls possible in any game.
     */
    public static final int MAX_ROLLS = 21;
    /**
     * Minimum number of rolls possible for a complete game.
     */
    public static final int MIN_ROLLS = 11;
    /**
     * Roll types used by {@link #rollToString()}.
     */
    private enum RollType {Normal, Strike, Spare};
     
    /**
     * Constructor.
     */
    public BowlingGame() {
        frames = new Frame[NUMBER_OF_FRAMES];
        for (int frameIndex = 0; frameIndex < NUMBER_OF_FRAMES; ++frameIndex) {
            frames[frameIndex] = new Frame();
        }
        init();
    }
    
    /**
     * Initialize the object.
     */
    public void init() {
        for (int frameIndex = 0; frameIndex < NUMBER_OF_FRAMES; ++frameIndex) {
            frames[frameIndex].init();
        }
        frames[NUMBER_OF_FRAMES - 1].isLast = true;
        currentFrameIndex = 0;
    }
   
    /**
     * Get the total score of the game so far.
     * Include scores from frames that are incomplete.
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
     */
    public boolean isFinished() {
        return frames[NUMBER_OF_FRAMES - 1].hasAllRolls();
    }
    
    /**
     * Get the frame the next roll should go in.
     * If no rolls more rolls can be made, return null.
     */
    public Frame nextRollFrame() {
        Frame frame = null;
        
        if (frames[currentFrameIndex].hasAllRolls()) {
            if (!frames[currentFrameIndex].isLast) {
                frame = frames[currentFrameIndex + 1];
            }
        } else {
            frame = frames[currentFrameIndex];
        }
        
        return frame;
    }
    
    /**
     * Add a roll to the game.
     * @param pinsDown Number of pins down in the roll.
     * @throws GameFinishedException
     * @throws InvalidRollException
     */
    public void addRoll(Roll roll) throws GameFinishedException, InvalidRollException {
        if (isFinished()) {
            throw new GameFinishedException();
        } else if (roll.pins() > nextRollFrame().pinsUp()) {
            throw new InvalidRollException();
        } else {
            // check for frame change
            if (frames[currentFrameIndex].hasAllRolls()) {
                // link the roll from the last frame to the new roll
                frames[currentFrameIndex].lastRoll.nextRoll = roll;
                ++currentFrameIndex;
            }
            
            frames[currentFrameIndex].addRoll(roll);
        }
    }
    
    /**
     * Get the set of possible rolls.
     * @return Zero sized result means no more rolls are possible
     * because the game is over.
     */
    public Collection<Roll> possibleRolls() {
        Frame frame = nextRollFrame();
        Collection<Roll> result = null;
        
        if (frame == null) {
            result = new ArrayList<Roll>();
        } else {
            result = frame.possibleRolls();
        }
        
        return result;
    }
    
    /**
     * Determine whether a roll would be a valid next
     * roll in the game.
     * @param roll A hypothetical roll.
     */
    public boolean canRoll(Roll roll) {
        Frame frame = nextRollFrame();
        boolean result = false;
        
        if (frame != null) {
            result = frame.canRoll(roll);
        }
        
        return result;
    }
   
    /**
     * Get an iterator over all the rolls in the game.
     */
    public Iterator<Roll> rollIterator() {
        return new RollIterator(frames[0].firstRoll, frames[currentFrameIndex].lastRoll);
    }
    
    /**
     * Convert roll to a string representation used in {@link #toString()}.
     * @param roll
     * @param type The type of roll.
     */
    protected static String rollToString(Roll roll, RollType type) {
        String result = null;
        
        switch (type) {
        case Normal:
            if (roll == null) {
                result = "";
            } else if (roll.pins() == 0) {
                result = "-";
            } else {
                result = "" + roll.pins();
            }
            break;
        case Strike:
           result = "X"; 
           break;
        case Spare:
            result = "/";
            break;
        default:
            break;
        }
       
        return result;
    }
    
    /**
     * Get a string representation of the score.
     */
    @Override
    public String toString() {
        String result = "|";
        String frameDivider = " | ";
        String emptyBox = " ";
        String skipSymbol = ".";
        String currentLocationSymbol = "@";
       
        // layout the frames in order
        for (int frameIndex = 0; frameIndex < NUMBER_OF_FRAMES; ++frameIndex) {
            Frame frame = frames[frameIndex];
            
            // put the frame score first
            String frameScore = null;
            if (frame.scoreIsComplete()) {
                frameScore = "" + frame.getScore();
            } else if (frame.getScore() == 0) {
                frameScore = " ? ";
            } else {
                frameScore = "" + frame.getScore() + "+?";
            }
            result += "(" + frameScore + ") ";
            
            // obtain values for first, second, third roll boxes
            if (frame.isLast) {
                String firstBox, secondBox, thirdBox;
                firstBox = secondBox = thirdBox = emptyBox;
                
                Iterator<Roll> rolls = frame.rollIterator();
                if (rolls.hasNext()) {
                    Roll firstRoll = rolls.next();
                    firstBox = rollToString(firstRoll, (frame.isStrike() ? RollType.Strike : RollType.Normal));
                    
                    if (rolls.hasNext()) {
                        Roll secondRoll = rolls.next();
                        if (frame.isSpare()) {
                            secondBox = rollToString(secondRoll, RollType.Spare);
                        } else {
                            secondBox = rollToString(secondRoll, RollType.Normal);
                        }
                        
                        if (rolls.hasNext()) {
                            Roll thirdRoll = rolls.next();
                            thirdBox = rollToString(thirdRoll, RollType.Normal);
                        } else if (frame == nextRollFrame()) {
                            thirdBox = currentLocationSymbol;
                        } else if (frame.hasAllRolls()) {
                            thirdBox = skipSymbol;
                        }
                    } else if (frame == nextRollFrame()) {
                        secondBox = currentLocationSymbol;
                    }
                } else if (frame == nextRollFrame()) {
                    firstBox = currentLocationSymbol;
                }
                
                result += firstBox + frameDivider + secondBox + frameDivider + thirdBox;
                
            } else {
                String firstBox, secondBox;
                firstBox = secondBox = emptyBox;
                
                Iterator<Roll> rolls = frame.rollIterator();
                if (rolls.hasNext()) {
                    Roll firstRoll = rolls.next();
                    firstBox = rollToString(firstRoll, (frame.isStrike() ? RollType.Strike : RollType.Normal));
                    
                    if (rolls.hasNext()) {
                        Roll secondRoll = rolls.next();
                        secondBox = rollToString(secondRoll, (frame.isSpare() ? RollType.Spare : RollType.Normal));
                    } else if (frame == nextRollFrame()) {
                        secondBox = currentLocationSymbol;
                    } else if (frame.hasAllRolls()) {
                        secondBox = skipSymbol; 
                    }
                } else if (frame == nextRollFrame()) {
                    firstBox = currentLocationSymbol;
                }
                
                result += firstBox + frameDivider + secondBox;
            }
          
            result += " |";
        }
        
        // add the total score
        result += "| Total: " + totalScore();
        
        return result;
    }
}
