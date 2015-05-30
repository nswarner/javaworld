/* Purpose
 *		
 *		The Config class holds configuration information for JavaWorld. Examples
 *		include the PORT, the adminName, the seedFile, ... The Config class also
 *		has methods to load the default configuration file and prompt to change
 *		the admin name.
 *		
 * Algorithm
 *		
 *		1. Declare the Config class
 *		2. Import the necessary classes, packages, interfaces, ...
 *		3. Declare a constant String to hold the Config file name
 *		4. Declare a constant int to hold the game port
 *		5. Declare Strings to hold the admin name and seed file name
 *		6. Declare a method to load the default config file
 *			- Try to open the file via Scanner object
 *			- Read in any comments; the first real String is the admin name
 *			- Read in any comments; the second real String is the seed file name
 *			- Close the file
 *			- Notify the admin what's been read in, set, and that the method was
 *				successful
 *		7. Declare a method to return the admin name
 *		8. Declare a method to set the admin name
 *		9. Declare a method to get the seed file name
 *		10. Declare a method to prompt the admin for JavaWorld's admin name
 *		
 * Structure / Process
 *		
 *		The Config class is used during the initial GameServer startup to offer
 *		the port, the admin name, and any other configuration values. The Config
 *		class may be used outside of the GameServer startup if needed.
 *		
 *	Author			- Nicholas Warner
 *	Created			- 4/24/2015
 *	Last Updated	- 5/1/2015
 */
 
 // Import the necessary packages, classes, interfaces, ...
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Config {

	// Constant String indicating main configuration file
	private static final String CONFIG_FILE	= "../config/main.cfg";

	// Port to listen to for socket connections
	public static final int PORT		= 5002;

	// Administrator Name
	private static String adminName 	= "";
	// seedFile for loading the pseudo-random generator
	private static String seedFile		= Seed.DEFAULT_SEED_FILE;
	// Message of the Day (MOTD) / Login Greeting
	private static String motd			= "";

	private static boolean newSeedFile	= false;

    // Accessor Methods
    public static String getAdmin() {
    	
    	return adminName;
    }
    
    public static String getSeedFileName() {
    	
    	return seedFile;
    }

	public static String getMOTD() {
		
		return motd;
	}

	public static boolean getNewSeedFile() {
		
		return newSeedFile;
	}

	// Mutator Methods    
    public static void setAdmin(String admin) {
    	
    	adminName = admin;
    }
    
    public static void setNewSeedFile(boolean updateRooms) {
    	
    	newSeedFile = updateRooms;
    }
    
	// Config Methods

	// Method to load the default configuration file
    public static void loadDefaultFile() {
    	
    	// Temporary holds one line of the MOTD when read in from file
    	String tempMOTD = "";
    	
    	// Try to read in from the CONFIG_FILE
		try {
			
			Scanner fileIn = new Scanner(new File(CONFIG_FILE));
			
			// The config file uses # for comments
			while((adminName = fileIn.nextLine()).charAt(0) == '#') {
				
				// Read in the comments, stop after reading the admin
			}
			
			// The config file uses # for comments
			while((seedFile = fileIn.nextLine()).charAt(0) == '#') {
				
				// Read in the comments, stop after reading our seed number
			}
			
			// Consume the motd comment
			fileIn.nextLine();
			
			// Read one line of the MOTD, continue reading until we hit #EOF
			while((tempMOTD = fileIn.nextLine()).charAt(0) != '#') {
				
				// Read in Message of the Day (MOTD), stop at #EOF
				motd += tempMOTD + "\n\r";
			}
			
			// Always close the file!
			fileIn.close();
			
			// Notify the Game Admin of what's been set
			System.out.println("Admin set to: " + adminName);
			System.out.println("Seed set to: " + seedFile);
			System.out.println("MOTD set.");
			System.out.println("Default config file loaded successfully.");
		}
		
		// Catch any Exceptions
		catch (IOException e) {
			
			// Notify the admin why we're exiting
			System.out.println("Issue when loading config file. Msg: " + e.getMessage());
			System.out.println("Exiting...");
			System.exit(0);
		}    	
    }
    
    // Method to prompt for a new Admin if needed
    public static void promptForAdmin() {
    	
    	// Declare an object for keyboard input
    	Scanner keyboard = new Scanner(System.in);
    	
    	// Prompt the game admin to input the new admin name
		System.out.print("Please input Admin name: ");
		
		// Accept the name
		adminName = keyboard.nextLine();

		// Let's make sure we have a capitalized Admin name
		adminName = adminName.toUpperCase().charAt(0) + adminName.substring(1);
		
		// Notify the game admin that the admin name has been set
		System.out.println("Admin set to: " + adminName);
    }
    
    // toString Method
    public String toString() {
    	
    	return "Class: Config";
    }
    
    // equals Method
    public boolean equals(Config oneConfig) {
    	
    	if (toString().equals(oneConfig.toString())) {
    		
    		return true;
    	}
    	
    	return false;
    }
}