import java.util.Serializable;

/**
 *<pre>
 * Purpose
 *		
 *		The Password class is meant to hold the password of a player and their profile separate
 *		from the Player itself. This helps ensure the password is not "floating around" and is
 *		very private.
 *		
 * Structure / Process
 *		
 *		The Password class consists of a String password, nothing more. It is created upon attempted
 *		login to compare against a password input by a user, then destroyed shortly thereafter. The
 *		Password itself is stored in the filesystem as a serialized object and is therefore not
 *		in plain text.
 *</pre>	
 * @author Nicholas Warner
 * @version 5.1, May 2015
 * @see Player
 * @see ManageSocketConnections
 */
public class Password implements Serializable {

	/** The password String to be saved / held. */
	String password;

	/** Default constructor sets the Password field to an empty String. */
    public Password() {

		password = "";
    }
    
    /** 
	 * Parameterized constructor takes a String argument and sets the password
	 * field to this argument.
	 *
	 * @param password The new password String being set.
	 */
    public Password(String password) {
    	
    	this.password = password;
    }
    
    /**
	 * A method to set or change a password.
	 *
	 * @param password The String to be set as the new Password.
	 */
    public void setPassword(String password) {
    	
    	this.password = password;
    }
    
    /** 
	 * A method to compare the given password with this Object's password.
	 *
	 * @param givenPassword A String to be compared against.
	 * @return Returns true if the passwords are equal and false if they
	 *			are not.
	 */
    public boolean comparePassword(String givenPassword) {
    	
    	if (password.equals(givenPassword)) {
    		
    		return true;
    	}
    	
    	return false;
    }
    
    /**
	 * A method to show what this Object is.
	 *
	 * @return Returns a String indicating the Object's type.
	 */
    public String toString() {
    	
    	return "Class: Password";
    }
    
	/** 
	 * A method to compare the given password with this Object's password.
	 *
	 * @param givenPassword A String to be compared against.
	 * @return Returns true if the passwords are equal and false if they
	 *          are not.
	 */
    public boolean equals(Password onePassword) {
    	
    	if (toString().equals(onePassword.toString())) {
    		
    		return true;
    	}
    	
    	return false;
    }
}
