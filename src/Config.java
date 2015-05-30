// Import the necessary packages, classes, interfaces, ...
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * <pre>
 * Purpose
 *
 * 	The Config class holds configuration information for JavaWorld. Examples
 *	include the PORT, the adminName, the seedFile, ... The Config class also
 *	has methods to load the default configuration file and prompt to change
 *	the admin name.
 *
 * Structure / Process
 * 
 *	The Config class is used during the initial GameServer startup to offer 
 *	the port, the admin name, and any other configuration values. The Config 
 *	class may be used outside of the GameServer startup if needed. This class 
 *	is declared as final with a private constructor. This infers that this 
 *	class is meant to be used in a static context and never meant to be 
 *	instantiated.
 * </pre>
 * @author Nicholas Warner
 * @version 5.1, May 2015
 * @see GameServer
 */
public final class Config {

	/** Constant String indicating main configuration file */
	private static final String CONFIG_FILE	= "../config/main.cfg";

	/** Port to listen to for socket connections */
	public static final int PORT		= 5002;

	/** Administrator Name */
	private static String adminName 	= "";
	/** seedFile for loading the pseudo-random generator */
	private static String seedFile		= Seed.DEFAULT_SEED_FILE;
	/** Message of the Day (MOTD) / Login Greeting */
	private static String motd			= "";

	/**
	 * Boolean which controls whether the game is loading with a new seed file
	 * or a previous seed file. This is important as the world is static when
	 * the seed file remains unchanged, which allows the characters to keep
	 * their discovered map. When the seed file is changed, the world's dynamic
	 * creation algorithm is called into action rebuilding a new world for the
	 * given seed file.
	 */
	private static boolean newSeedFile	= false;

	/**
	 * This class is meant to be uninstantiable. Each method and field are
	 * declared static. By making the constructor private, we ensure that
	 * this class cannot be instantiated.
	 */
	private Config() {

		/*	
		 *	Strictly forces the class to never be instantiated, even if called
		 *	from within the class itself.
		 */
		throw new AssertionError();
	}

    /** 
	 * Method to get the current Administrator's name.
	 *
	 * @return Administrator's name as a String.
	 */
    public static String getAdmin() {
    	
    	return adminName;
    }
    
	/**
	 * Method to get the seed file's name.
	 *
	 * @return Seed File's name.
	 */
    public static String getSeedFileName() {
    	
    	return seedFile;
    }

	/**
	 * Method to get the Message of the Day (MOTD)
	 *
	 * @return The MOTD.
	 */
	public static String getMOTD() {
		
		return motd;
	}

	/**
	 * Method to get whether the seed file has changed or not
	 *
	 * @return the newSeedFile boolean value
	 */
	public static boolean getNewSeedFile() {
		
		return newSeedFile;
	}

	/**
	 * Method to set the name of the Administrator once the game is running.
	 *
	 * @param admin A String which represents the new Administrator's name
	 */
    public static void setAdmin(String admin) {
    	
    	adminName = admin;
    }

	/**
	 * Method to set whether a new seed file has been generated or not
	 *
	 * @param updateRooms A boolean which indicates whether a new seed file
	 *					  is loaded or not.
	 */
    public static void setNewSeedFile(boolean updateRooms) {
    	
    	newSeedFile = updateRooms;
    }
    
	// Config Methods

	/** 
	 * Method to load the default configuration file. This file is loaded
	 * from the CONFIG_FILE constant.
	 *
	 * @see #CONFIG_FILE
	 */
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
			System.out.println("Issue when loading config file. Msg: " + 
								e.getMessage());
			System.out.println("Exiting...");
			System.exit(0);
		}    	
    }
    
    /** Method to prompt for a new Admin if needed. */
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
}
