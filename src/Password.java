/* Purpose
 *		
 *		The Password class is meant to hold the password of a player and their profile separate
 *		from the Player itself. This helps ensure the password is not "floating around" and is
 *		very private.
 *		
 * Algorithm
 *		
 *		1. Declare the Password class
 *		2. Import the Serializable interface
 *		3. Declare the String password
 *		4. Declare a default constructor
 *		5. Declare a paramterized constructor
 *		6. Declare a method to set the password
 *		7. Declare a method to compare a given password with the object's password
 *		8. Declare the toString method and equals method
 *		
 * Structure / Process
 *		
 *		The Password class consists of a String password, nothing more. It is created upon attempted
 *		login to compare against a password input by a user, then destroyed shortly thereafter. The
 *		Password itself is stored in the filesystem as a serialized object and is therefore not
 *		in plain text.
 *		
 *	Author			- Nicholas Warner
 *	Created			- 4/24/2015
 *	Last Updated	- 5/1/2015
 */

// Import Serializable
import java.io.Serializable;

// Implement Serializable to store data as an object
public class Password implements Serializable {

	// The password itself
	String password;

	// Default Constructor
    public Password() {

		password = "";
    }
    
    // Paramterized Constructor
    public Password(String password) {
    	
    	this.password = password;
    }
    
    // Mutator Method
    public void setPassword(String password) {
    	
    	this.password = password;
    }
    
    // Method to compare the given password with the object's password
    public boolean comparePassword(String givenPassword) {
    	
    	if (password.equals(givenPassword)) {
    		
    		return true;
    	}
    	
    	return false;
    }
    
    // toString Method
    public String toString() {
    	
    	return "Class: Password";
    }
    
    // equals Method
    public boolean equals(Password onePassword) {
    	
    	if (toString().equals(onePassword.toString())) {
    		
    		return true;
    	}
    	
    	return false;
    }
}