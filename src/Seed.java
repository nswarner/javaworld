import java.io.*;
import java.util.Random;
import java.util.Scanner;

/**
 *<pre>
 *	Purpose
 *		
 *		JavaWorld allows for randomly generating different rooms and sections of the world. To
 *		allow for the admin to maintain a persistent world, we create a seed file with a large
 *		number of predefined pseudo-random doubles which are then used to seed the creation of
 *		the randomly generated rooms. If the admin would like a new layout, they may generate
 *		a new seed file on game startup (or save the current seed and generate a new seed).
 *		
 *	Structure / Process
 *		
 *		The Seed class is meant to hold or generate a list of predefined pseudo
 *		random numbers which are used for generating the dynamic world. If the
 *		same seed file is used repeatedly, the world will remain persistent, but
 *		if the seed file is newly generated, the world will be different and
 *		new.
 *</pre>
 * @author Nicholas Warner
 * @version 5.1, June 2015
 * @see Room
 * @see Item		
 */
public final class Seed {

	/** The generic Seed File to load from. */
	public final static String DEFAULT_SEED_FILE	= "../seed/1.seed";

	/** The number of Random Doubles in Generic Seed File. */
	public final static int SEED_FILE_SIZE	= 10000;

	/** The instance variable which holds the (working) seed file's name. */
	private static String seedFileName = "";

	/** An array to hold the pseudo-random array of doubles. */
	private static double[] randomList	= new double[10000];
	/** An int to hold the position in the randomList. */
	private static int positionInList	= 0;

	/** A private constructor ensures this class cannot be instantiated. */
	private Seed() {

		throw new AssertionError();
	}

	/** 
	 * A method to return the seed file's name.
	 *
	 * @return Returns a String with the name of the seed file.
	 */
	public static String getSeedFile() {
		
		return seedFileName;
	}
	
	/** 
	 * A method to set the seed file's name.
	 *
	 * @param seedFile The new name of the seed file.
	 */
	public static void setSeedFile(String seedFile) {
		
		seedFileName = seedFile;
	}

	/** 
	 * A method to generate a new seed file.
	 *
	 * @param seedFileName The name of the seed file to load the seeds from.
	 * @param numOfSeeds The number of seeds to load from the file.
	 */
	public static void generateSeed(String seedFileName, int numOfSeeds) {

		// Declare a new random number generator		
		Random rng = new Random();
		
		// Ensure that we have a file name
		if (seedFileName.equals("")) {
			
			seedFileName = DEFAULT_SEED_FILE;
		}
		
		// Try to open the seed file for writing
		try {

			// Open the given seed file for writing
			PrintWriter fileOut = new PrintWriter(new File(seedFileName));
			
			// Print out the given number of seeds into the file
			for(int i = 0; i < numOfSeeds; i++) {
				
				fileOut.print(rng.nextDouble() + " ");
			}
			
			// Close the file
			fileOut.close();
			
			// Notify the admin that the seed file was generated properly
			System.out.println("Seed file \"" + seedFileName + "\" generated successfully.");
			
			Config.setNewSeedFile(true);
		}
		
		// Catch any thrown exceptions
		catch (IOException e) {
			
			System.out.println("Exception in Seed.GenerateSeeds. Msg: " + e.getMessage());
			System.out.println("Exiting...");
			System.exit(0);
		}
	}
	
	/** A method to load the pseudo-random doubles into the array. */
	public static void loadRandomNumbers() {
		
		// If the file name hasn't been set, set it to the default
		if (seedFileName == "") {
			
			seedFileName = DEFAULT_SEED_FILE;
		}
		
		// Try to open the file and read in the given double list
		try {

			// Declare a Scanner object for file input
			Scanner fileIn = new Scanner(new File(seedFileName));
			
			// Read in the large number of seeds
			for(int i = 0; i < 10000; i++) {
				
				//System.out.println("Test1");
				randomList[i] = fileIn.nextDouble();
				//System.out.println("Test2");
				
				if (i % 10 == 0) {
					
				//	System.out.println("test: " + randomList[i]);
				}
			}
			
			// Close the file
			fileIn.close();
			
			// Notify the admin that the seed file loaded successfully
			System.out.println("Random Generator [" + seedFileName + "]" + "loaded successfully.");
		}
		
		// Catch any thrown errors
		catch (IOException e) {
			
			System.out.println("Exception in loadRandomNumbers. Msg: " + e.getMessage());
			System.out.println("Exiting...");
			System.exit(0);
		}
	}
	
	/** 
	 * A method to return one number from the pseudo-random list then increment to the next.
	 *
	 * @return Returns the next number within the randomList of Pseudo-random numbers.
	 */
	public static double rand() {
		
		// If we've hit the end, let's start again
		if (positionInList == 10000) {
			
			positionInList = 0;
		}
		
		//System.out.println(positionInList + "][" + randomList[positionInList] + " ...");
		
		// Return the next number in the list
		return randomList[positionInList++];
	}
	
	/** A method which requests the admin to choose whether they want a new seed or not. */
	public static void promptForSeed() {
		
		// Declare keyboard input
		Scanner keyboard = new Scanner(System.in);
		// Declare a String to hold the keyboard input
		String userInput = "";
		
		// Prompt the user if they'd like to generate a new seed
		System.out.print("Generate a new Seed (y/n)? ");
		
		// Accept their answer
		userInput = keyboard.nextLine().trim().toLowerCase();
		
		// If the user wants to, generate the file, otherwise don't
		if (userInput.length() > 0 && userInput.charAt(0) == 'y') {
			
			generateSeed(DEFAULT_SEED_FILE, SEED_FILE_SIZE);
		}
		
		// Don't generate the new file if the user didn't say yes
		else {
			
			System.out.println("No new seed generated.");
		}

		// Reset the seed file name		
		seedFileName = DEFAULT_SEED_FILE;
	}
}
