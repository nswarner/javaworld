/** 
 *<pre>
 * Purpose
 *		
 *		The Hangman class represents the Hangman minigame available within
 *		the main game. A player types "hangman (start / status / play [letter])"
 *		to participate in the game of hangman.
 *		
 * Structure / Process
 *		
 *		Hangman is a minigame developed to be played by players within the game.
 *		The hangman command is called by the player's socket input and interpreted
 *		within the Interpreter class. The Interpreter class has a commandHangman
 *		which identifies whether it's a start, play, status request, and then
 *		makes the necessary adjustments to the Hangman class (as it's static).
 *</pre>
 * @author Nicholas Warner
 * @version 5.1, May 2015
 */
public final class Hangman {

	/** A String to hold the mystery word. */
	private static String mysteryWord;
	/** A boolean array to hold the used characters. */
	private static boolean[] charactersUsed;
	/** A boolean to manage whether the game has ended or not. */
	private static boolean gameEnded;
	/** An int to hold the number of wrong guesses. */
	private static int numWrong;

	/** Private constructor forces an uninstantiable class. */
	private Hangman() {

		throw new AssertionError();
	}

	/** 
	 * A method to start a new game.
	 *
	 * @param mWord The given mystery word for the new game of Hangman.
	 */
	public static void newGame(String mWord) {
		
		// Set the new mystery word
		mysteryWord = mWord.toLowerCase();
		// Clear our boolean array
		charactersUsed = new boolean[26];
		// Reset the lives lost counter
		numWrong = 0;
	}

	/** 
	 * A method to play a letter if possible.
	 *
	 * @param letter The letter being played in the mystery word
	 * @return A true or false value indicating that the letter was either
				allowed or not.
	 */
	public static boolean tryLetter(char letter) {
	
		// To see whether it was successful or not
		boolean foundLetter = false;

		// If the game hasn't ended
		if (!gameEnded) {
	
			// Check whether the given letter is acceptable
			if (charactersUsed[letter - 97] || letter < 97 || letter > 123) {
			
				// If not, notify caller that we did nothing
				return false;
			}
			
			// Declare the letter used
			charactersUsed[letter - 97] = true;
	
			// Test whether the letter was in the mystery word or not
			for(int i = 0; i < mysteryWord.length(); i++) {
				
				// If it's in the mystery word
				if (letter == (char)i) {
					
					// We don't lose a life!
					foundLetter = true;
				}
			}
	
			// Otherwise, not in the word
			if (!foundLetter) {
				
				// We lose a life
				numWrong++;
				
				// If they really lost
				if (numWrong >= 7) {
					
					// Then the game ends
					gameOverLoss();
				}
			}
	
			// Check whether the game has ended in a win
			testWin();	
			// The letter was okay to use, even if not in word
			return true;
		}
		
		// The letter was bad and you should feel bad
		return false;
	}

	/** 
	 * A method to test whether the mystery word has been completed from the 
	 * boolean array.
	 */
	public static void testWin() {
		
		// Assume it has
		boolean notFound = false;

		// Ensure that the game hasn't already ended
		if (!gameEnded) {
		
			// For each letter in the mystery word
			for(int i = 0; i < mysteryWord.length(); i++) {
				
				// Take the letter
				char letter = mysteryWord.charAt(i);
				
				// And check whether it's been used in our char array
				if (!charactersUsed[letter - 97]) {
					
					// It hasn't, so that's good enough
					notFound = true;
				}
			}
			
			// Otherwise, it has and it's a win!
			if (!notFound) {
				
				gameOverWin();
			}
		}
	}

	/** A method to notify the players that the game has ended in a loss */
	public static void gameOverLoss() {
		
		// Notify via info
		Player.infoAll("#GHangman Game Over! #RPlayers Lose.");
		// Set the boolean to game over
		gameEnded = true;
	}
	
	/** A method to notify the players that the game has ended in a win */
	public static void gameOverWin() {
		
		// Notify via info
		Player.infoAll("#GHangman Game Over! #YPlayers Win!");
		// Set the boolean to game over
		gameEnded = true;
	}

	/** 
	 * A method to see how many lives lost.
	 *
	 * @return Returns the value of the numWrong field
	 */
	public static int getWrong() {
		
		return numWrong;
	}
	
	/**
	 * String method to build the actual hangman graphic.
	 *
	 * @return Returns a String with the current Hangman status.
	 */
	public static String getStatus() {
	
		// The String to hold the output
		String tempOut = "Mystery Word: ";
		
		// Loop through each letter
		for(int i = 0; i < mysteryWord.length(); i++) {
		
			char letter = mysteryWord.charAt(i);
			
			// If it's been used
			if (charactersUsed[letter - 97]) {
			
				// Allow it to be shown
				tempOut += " " + letter + " ";
			}
			
			// If not
			else {
			
				// Show a blank
				tempOut += " _ ";
			}
		}
		
		// Build the actual hangman person
		tempOut += "\n\r";
		tempOut += "       _________________\n\r";
		tempOut += "       |               |\n\r";
		tempOut += "       |               " + (numWrong > 0 ? "O" : " ") + "\n\r";
		tempOut += "       |              " + (numWrong > 1 ? "\\" : " ") +
											  (numWrong > 2 ? "|" : " ") +
											  (numWrong > 3 ? "/" : " ") + "\n\r";
		tempOut += "       |               " + (numWrong > 4 ? "|" : " ") + "\n\r";
		tempOut += "   ____|____          " + (numWrong > 5 ? "/ " : "  ") + 
											  (numWrong > 6 ? "\\" : " ") + "\n\r";
		tempOut += "  /         \\\n\r";
		tempOut += " /           \\\n\r";

		// Notify the player of the letters which have been used
		tempOut += "Letters Used: ";

		// Loop through the characters used array and display
		for(int i = 0; i < 26; i++) {
			
			if (charactersUsed[i]) {
				
				tempOut += (char) (i + 97) + " ";
			}
		}

		// Return the built String
		return tempOut;
	}
}
