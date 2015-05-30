/* Purpose
 *		
 *		The Coordinate class allows for an object to represent a point on the (x, y) plane of
 *		JavaWorld. This makes it easy to pass around a room location, item location, or player
 *		location, instead of having to pass two separate integers.
 *		
 * Algorithm
 *		
 *		1. Declare the Coordinate class
 *		2. Declare two ints to represent the X and Y coordinates respectively
 *		3. Create a default constructor and a parameterized constructor
 *		4. Create accessor and mutator methods for both variables
 *		5. Create a toString method
 *		6. Create an equals method
 *		
 * Structure / Process
 *		
 *		The Coordinate class is used within JavaWorld at several locations to handle passing x
 *		and y coordinates.
 *		
 *	Author			- Nicholas Warner
 *	Created			- 4/24/2015
 *	Last Updated	- 5/1/2015
 */
 
public class Coordinate {

	// Declare our variables
	private int coordX;
	private int coordY;

	// Declare our default constructor
	public Coordinate() {
    
    	// Set them to values which are null-equivalent for a positive (x, y) plane
    	coordX = -1;
    	coordY = -1;
    }

	// Declare our parameterized constructor
    public Coordinate(int x, int y) {
    	
    	coordX = x;
    	coordY = y;
    }

	// Accessor Methods
    public int getCoordX() {
    	
    	return coordX;
    }
    
    public int getCoordY() {
    	
    	return coordY;
    }

	// Mutator Methods
    public void setCoordinateX(int x) {
    	
    	coordX = x;
    }
    
    public void setCoordinateY(int y) {
    	
    	coordY = y;
    }    
    
    // toString Method
    public String toString() {
    	
    	return "Class: Coordinate\nCoordX: " + coordX + "\nCoordY: " + coordY;
    }
    
    // equals Method
    public boolean equals(Coordinate oneCoordinate) {
    	
    	if (toString().equals(oneCoordinate.toString())) {
    		
    		return true;
    	}
    	
    	return false;
    }
}