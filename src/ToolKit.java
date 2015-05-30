/**
 *	Purpose
 *
 *		The ToolKit class holds methods which are useful but don't necessarily have a spot in
 *		other classes. 
 *
 *	Algorithm
 *
 *		1. Declare the ToolKit class
 *		2. Import the StringTokenizer class
 *		3. Declare a slow method (deprecated; initial testing purposes only)
 *		4. Declare a hasNumber method to test whether a String has a number or not
 *		5. Declare a hasQuotes method to test whether a String has quotes or not
 *		6. Declare a rand method to return a random number between upper and lower bounds
 *		7. Declare a toString method
 *		8. Declare an equals method
 *
 *	Structure / Process
 *
 *		Methods are used statically as needed.
 *
 *	Author			- Nicholas Warner
 *	Created 		- 4/24/2015
 *	Last Updated	- 4/30/2015
 */

// Import necessary packages, classes, interfaces, etc.
import java.util.StringTokenizer;

public class ToolKit {
	
	// Deprecated
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

	// A method used to pull a random number between an upper and lower bound    
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

	// toString method    
    public String toString() {
    	
    	return ("Class: ToolKit");
    }
    
    // Equals method
    public boolean equals(ToolKit oneToolKit) {
    	
    	if (oneToolKit.toString().equals(toString())) {
    		
    		return true;
    	}
    	
    	return false;
    }
}
