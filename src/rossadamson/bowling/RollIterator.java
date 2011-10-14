package rossadamson.bowling;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator for the rolls in this frame.
 * @author Ross Adamson
 */
public class RollIterator implements Iterator<Roll> {
    private Roll currentRoll;
    private Roll endRoll;
        
    /**
     * Construct the iterator.
     * Precondition: start and end rolls must be in the same linked list
     * and end must come after start.
     * @param start The first Roll to iterate over.
     * @param end The last Roll to iterate over.
     */
    public RollIterator(Roll start, Roll end) {
        endRoll = end;
        currentRoll = start;
    }
        
    @Override
    public boolean hasNext() {
        return (endRoll != null) && (currentRoll != endRoll.nextRoll);
    }

    @Override
    public Roll next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
            
        Roll result = currentRoll;
        if (result != null) {
            currentRoll = result.nextRoll;
        }
        return result;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
	