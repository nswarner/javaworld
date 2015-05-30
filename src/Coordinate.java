/**
 *<pre>
 * Purpose
 *		
 *	The Coordinate class allows for an object to represent a point on the 
 *	(x, y) plane of JavaWorld. This makes it easy to pass around a room 
 *	location, item location, or player location, instead of having to pass two 
 *	separate integers.
 *		
 * Structure / Process
 *		
 *	The Coordinate class is used within JavaWorld at several locations to 
 *	handle passing x and y coordinates.
 *</pre>		
 * @author Nicholas Warner
 * @version 5.1, May 2015
 */
public class Coordinate {

	/** Holds the X coordinate value. */
	private int coordX;
	/** Holds the Y coordinate value. */
	private int coordY;

	/**
	 * Default constructor for the Coordinate class. X and Y are initialized
	 * to -1.
	 */
	public Coordinate() {
    
    	// Set them to values which are null-equivalent for a positive (x, y) plane
    	coordX = -1;
    	coordY = -1;
    }

	/**
	 * Parameterized constructor for the Coordinate class. X and Y are
	 * initialized to the given arguments.
	 *
	 * @param x Initializes the coordX field.
	 * @param y Initializes the coordY field.
	 */
    public Coordinate(int x, int y) {
    	
    	coordX = x;
    	coordY = y;
    }

	/** 
	 * Method to return the value of the X coordinate.
	 * 
	 * @return The value of the coordX field.
	 */
    public int getCoordX() {
    	
    	return coordX;
    }
    
	/** 
	 * Method to return the value of the Y coordinate.
	 *
	 * @return The value of the coordY field.
	 */
    public int getCoordY() {
    	
    	return coordY;
    }

	/**
	 * Method to set the value of the X coordinate.
	 *
	 * @param x Sets the value of the coordX field.
	 */
    public void setCoordinateX(int x) {
    	
    	coordX = x;
    }
    
	/**
	 * Method to set the value of the Y coordinate.
	 *
	 * @param y Sets the value of the coordY field.
	 */
    public void setCoordinateY(int y) {
    	
    	coordY = y;
    }    
    
    /**
	 * Method which returns the given Object's coordinate values.
	 *
	 * @return A String representing the class and its (coordX, coordY) fields.
	 */
    public String toString() {
    	
    	return "Class: Coordinate\nCoordX: " + coordX + "\nCoordY: " + coordY;
    }
    
    /**
	 * Method which tests whether two given Coordinate Objects are equal.
	 * 
	 * @param oneCoordinate A Coordinate object to test against this.
	 * @return A boolean value indicating whether the objects are equal.
	 */
    public boolean equals(Coordinate oneCoordinate) {

		// This is a fairly lazy way, but it does ensure accuracy
    	if (toString().equals(oneCoordinate.toString())) {
    		
    		return true;
    	}
    	
    	return false;
    }
}
