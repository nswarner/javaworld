import java.util.StringTokenizer;

/**
 *<pre>
 *	Purpose
 *
 *		The ToolKit class holds methods which are useful but don't necessarily have a spot in
 *		other classes. 
 *
 *	Structure / Process
 *
 *		Methods are used statically as needed.
 *</pre>
 * @author Nicholas Warner
 * @version 5.1, June 2015
 */
public final class ToolKit {

	/** The private constructor ensuring the class cannot be instantiated. */
	private ToolKit() {

		throw new AssertionError();
	}

	/**
	 * A method to slow down the game's main loop.
	 *
	 * @param loopsPerSecond The game should slow to this many loops per second.
	 */
	public static void slow(long loopsPerSecond) {
		
		// Avoid a divide by 0 Exception
		if (loopsPerSecond == 0) {
			
			loopsPerSecond = 10;
		}
		
		// Sleep the current thread
		try {
			
			Thread.sleep(1000 / loopsPerSecond);
		}
		
		// Catch any Exceptions
		catch (Exception e) {
			
			System.out.print("Exception in ToolKit.slow: " + e.getMessage());
		}
	}

	/**
	 * A method used to pull a random number between an upper and lower bound.
	 *
	 * @param lower The lower bound of the random number.
	 * @param upper The upper bound of the random number.
	 * @return Returns an int between lower and upper.
	 */
    public static int rand(int lower, int upper) {
        
        // Get the random number
        double rNum = Math.random() * upper;
        
        // If it's below the lower bound
        if (rNum < lower) {
            
            // Floor it
            rNum = lower;
        }
        
        // Return the number as an int
        return (int)rNum;
    }
}
