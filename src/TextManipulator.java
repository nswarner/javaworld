import java.util.StringTokenizer;

/**
 *<pre>
 * Purpose
 *		
 *		This class manipulates Strings in different ways as needed. Also handles ANSI color codes.
 *		
 * Structure / Process
 *		
 *		All Strings sent to the player through the message method are run through the colorizer
 *		method in this class (called "addColor"). Beyond that, a lot of the functionality has
 *		to do with taking two paragraphs and making them into a two-column fit (automap + 
 *		room description).
 *</pre>	
 * @author Nicholas Warner
 * @version 5.1, June 2015	
 */
public final class TextManipulator {

	/** The normal color of text. */
	private static final String ANSI_RESET		= "\033[0;0m";
	/** The dull black color of text. */
	private static final String ANSI_DULLBLACK	= "\033[0;30m";
	/** The dull red color of text. */
	private static final String ANSI_DULLRED	= "\033[0;31m";
	/** The dull green color of text. */
	private static final String ANSI_DULLGREEN	= "\033[0;32m";
	/** The dull yellow color of text. Possibly brown. */
	private static final String ANSI_DULLYELLOW = "\033[0;33m";
	/** The dull blue color of text. */
	private static final String ANSI_DULLBLUE	= "\033[0;34m";
	/** The dull purple color of text. */
	private static final String ANSI_DULLPURPLE = "\033[0;35m";
	/** The dull cyan color of text. */
	private static final String ANSI_DULLCYAN	= "\033[0;36m";
	/** The dull white (silver/grey) color of text. */
	private static final String ANSI_DULLWHITE	= "\033[0;37m";

	/** The strong grey color of text. */
	private static final String ANSI_GREY		= "\033[1;30m";
	/** The bright red color of text. */
	private static final String ANSI_RED		= "\033[1;31m";
	/** The bright green color of text. */
	private static final String ANSI_GREEN		= "\033[1;32m";
	/** The bright yellow color of text. */
	private static final String ANSI_YELLOW		= "\033[1;33m";
	/** The bright blue color of text. */
	private static final String ANSI_BLUE		= "\033[1;34m";
	/** The bright purple color of text. */
	private static final String ANSI_PURPLE		= "\033[1;35m";
	/** The bright cyan color of text. */
	private static final String ANSI_CYAN		= "\033[1;36m";
	/** The bright white color of text. */
	private static final String ANSI_WHITE		= "\033[1;37m";

	/** The private constructor, ensuring this class cannot be instantiated. */
	private TextManipulator() {

		throw new AssertionError();
	}

	/**
	 * A method to add color to the given String and return the colorized String.
	 *
	 * @param text The String to be colorized.
	 * @return Returns a String where color codes are replaced by their ansi codes.
	 */
	public static String addColor(String text) {
		
		// This holds the text we'll actually send back
		String rebuiltText = "";
		
		// We go character by character through the String
		for(int i = 0; i < text.length(); i++) {
						
			// At each character, we look for a # symbol which indicates color
			switch(text.charAt(i)) {

				// Found one
				case '#':

					// And the following character indicates WHAT color
					switch(text.charAt(i+1)) {

						// #n reset the color
						case 'n':
							rebuiltText += ANSI_RESET;
							i++;	break;

						// #g dull green
						case 'g':
							rebuiltText += ANSI_DULLGREEN;
							i++;	break;

						// #G bright green						
						case 'G':
							rebuiltText += ANSI_GREEN;
							i++;	break;

						// etc..
						case 'y':
							rebuiltText += ANSI_DULLYELLOW;
							i++;	break;

						case 'Y':
							rebuiltText += ANSI_YELLOW;
							i++;	break;

						case 'r':
							rebuiltText += ANSI_DULLRED;
							i++;	break;
						
						case 'R':
							rebuiltText += ANSI_RED;
							i++;	break;	
						
						case 'b':
							rebuiltText += ANSI_DULLBLUE;
							i++;	break;
						
						case 'B':
							rebuiltText += ANSI_BLUE;
							i++;	break;
						
						case 'p':
							rebuiltText += ANSI_DULLPURPLE;
							i++;	break;
						
						case 'P':
							rebuiltText += ANSI_PURPLE;
							i++;	break;
						
						case 'c':
							rebuiltText += ANSI_DULLCYAN;
							i++;	break;
						
						case 'C':
							rebuiltText += ANSI_CYAN;
							i++;	break;
						
						case 'w':
							rebuiltText += ANSI_DULLWHITE;
							i++;	break;
						
						case 'W':
							rebuiltText += ANSI_WHITE;
							i++;	break;
						
						case 's':
							rebuiltText += ANSI_DULLBLACK;
							i++;	break;
						
						default:
							rebuiltText += text.charAt(i);
							break;
					}

					break;
	
				default:
					rebuiltText += text.charAt(i);
					break;
			}
		}
		
		// And after we've replaced #C's with \033[1;36m (escape sequence for color), we return it
		return rebuiltText;
	}

	/**
	 * A method which pulls one argument from the given String and returns it or returns "".
	 *
	 * @param argument The String which holds the argument to be pulled.
	 * @return Returns a String containing the first token / word from the given argument.
	 */
	public static String oneArgument(String argument) {
		
		StringTokenizer st = new StringTokenizer(argument);
		
		if (st.hasMoreTokens()) {
			
			return (st.nextToken());
		}
		
		return "";
	}

    /**
	 * A method which takes a fixed width chunk of text, chunkOne, and aligns a second column
	 * against the first column. Then you end up with two columns of text.
	 *
	 * @param chunkOne The String which contains the fixed width left column of text.
	 * @param chunkTwo The String to be placed against the left side column.
	 * @return Returns a String with two columns of text.
	 * @see Interpreter#commandMap(Player)
	 */
	public static String smushLeft(String chunkOne, String chunkTwo) {

		// Declare two Strings which act as single line chunks
		String colOne = "";
		String colTwo = "";	

		// Used for buffered spacing
		final String emptyString = "                                               " +
								   "                                 ";

		// The location of the next newLine character in each column
		int newLineColOne = 0;
		int newLineColTwo = 0;

		// The width of the column (ignoring the unseen color characters)
		int widthOfColumn = findColorIndexOf(chunkOne, '\n') + 1;
		
		// So long as there's at least one line from either column to write
		while(chunkOne.length() > 0 || chunkTwo.length() > 0) {
			
			// Find the next newline
			newLineColOne = chunkOne.indexOf("\n") + 1;
			newLineColTwo = chunkTwo.indexOf("\n") + 1;

			// If our left column is empty
			if (chunkOne.length() == 1) {
				
				// We fill it out to make it even
				chunkOne += emptyString.substring(0, widthOfColumn - 1);
			}
									
			// The final one column is given the left column's one line chunk
			colOne += oneLine(chunkOne, newLineColOne) + "\t" + (chunkOne.equals("") ? "\t" : "");
			// The final one column is given the right column's one line chunk
			colOne += oneLine(chunkTwo, newLineColTwo) + "\n\r";
			// The left chunk is reduced by a single line
			chunkOne = consumeALine(chunkOne, newLineColOne);
			// The right chunk is reduced by a single line
			chunkTwo = consumeALine(chunkTwo, newLineColTwo);
		}

		// Return the merged String				
		return colOne;
	}

	/** 
	 * A method to split a given string at newLine intervals. This can be used to
	 * generate a fixed width column of text from a given String.
	 *
	 * @param chunk The String to be split into intervals.
	 * @param newLine The location of where newlines should be placed.
	 * @return Returns a String that has a fixed column width of newLine in length.
	 * @see #smushLeft
	 */
	public static String splitAString(String chunk, int newLine) {

		// Temporary String Storage
		String output = "";
		// A counter to hold the current length
		int lengthCounter = 0;

		// Iterate through the chunk
		for(int i = 0; i < chunk.length(); i++) {

			// If we find a possible color code, we might want to skip it for length
			if (chunk.charAt(i) == '#') {
				
				// Test whether it is a color code or not
				switch(chunk.charAt(i + 1)) {
					
					// It is a color code so we skip ahead
					case 'g':
					case 'b':
					case 'r':
					case 'y':
					case 'n':
					case 'p':
					case 'c': i += 1; break;
					// It isn't a color code so we count it
					default: lengthCounter++;
				}
			}
			
			// Otherwise it's definitely a letter and we definitely count it
			else {
				
				lengthCounter++;
			}
			
			// If we find a new line
			if (chunk.charAt(i) == '\n') {
				
				// We respect that it's a new line and add it
				output += chunk.substring(0, i + 1);
				// Then remove that first chunk
				chunk = chunk.substring(i + 1);
				// Reset our line counter
				i = 0;
				// Reset our lengthCounter
				lengthCounter = 0;
			}
			
			// If we've hit the length before we want an auto-newline, then
			else if (lengthCounter % newLine == 0 && lengthCounter != 0) {
				
				// We take the line and add it to our output
				output += chunk.substring(0, i) + "\n";
				// We remove that added line
				chunk = chunk.substring(i);
				// Reset our line counter
				i = 0;
				// Reset our length counter
				lengthCounter = 0;
			}
		}
		
		// Add the remaining chunk
		output += chunk;
		// Return the split output
		return output;
	}
	
	/**
	 * A method to pull a single line from a given String.
	 *
	 * @param chunk The String which a single line will be pulled from.
	 * @param length The length of the line to be pulled. If a newline is
	 *					found before length, then that is returned, otherwise
	 *					a line is returned up to length in length.
	 * @return Returns a String containing the single line pulled from chunk.
	 */
	public static String oneLine(String chunk, int length) {
		
		// The line that'll be pulled
		String line = "";
		// We find the next newline
		int newLineLocation = chunk.indexOf("\n");
		
		// If we want more than we can give or there's no \n
		if (newLineLocation == -1 || newLineLocation > length) {
			
			// We return the original String (give or take)
			line = chunk.substring(0, chunk.length());
		}

		// Otherwise, we pull the given chunk out
		else {
			
			line = chunk.substring(0, chunk.indexOf("\n"));
		}
		
		// Return the built line
		return line;
	}
	
	/**
	 * A method to remove a single line from a given String.
	 *
	 * @param chunk The original String before the line is removed.
	 * @param length The length of the line to be removed.
	 * @return Returns a String containing the original text from chunk but
	 *			with length characters removed OR the first line removed which
	 *			is constituted by the first newline character.
	 */
	public static String consumeALine(String chunk, int length) {
		
		// A temporary String
		String line = "";
		// The location of the next newline
		int newLineLocation = chunk.indexOf("\n");
		
		// If it's an empty String, there are no \n's, or the next \n is a long way off
		if (newLineLocation == -1 || newLineLocation > length) {
			
			// Then we return the original String (give or take)
			line = chunk.substring(chunk.length());
		}
		
		// Otherwise, we pull the given chunk out
		else {
			
			line = chunk.substring(chunk.indexOf("\n") + 1);
		}
		
		// Return the rebuilt line
		return line;
	}
	
	/**
	 * A method to find the index of a given character in a given String ignoring color codes.
	 *
	 * @param search The String intended to be searched.
	 * @param character The character to be found in the String.
	 * @return Returns an int which represents the location of the given character while
	 *			ignoring color codes.
	 */
	public static int findColorIndexOf(String search, char character) {
		
		// A counter for the length (or index of the character)
		int lengthCounter = 0;
		
		// Loop through the search String
		for(int i = 0; i < search.length(); i++) {
			
			// If we hit a possible color code, check if it's actually a color code
			if (search.charAt(i) == '#') {
				
				// Test the following character
				switch(search.charAt(i + 1)) {

					// Success, it's color					
					case 'g':
					case 'b':
					case 'r':
					case 'y':
					case 'n':
					case 'p':
					case 'c': i += 1; break;
					// It is most definitely not color
					default: break;
				}
			}
			
			// If we actually found the right character
			else if (search.charAt(i) == character) {
				
				// Then that's the length
				return lengthCounter;
			}
			
			// Otherwise, we keep searching
			else {
				
				lengthCounter++;
			}
		}
		
		// We did not find the given character in the given String
		return -1;
	}
        
    /**
	 * A method which gets the nth word from a given String argument.
	 *
	 * @param argument The String argument which holds the nth word.
	 * @param n The number of words / tokens to pass by.
	 * @param all Currently not implemented.
	 * @return Returns a String which contains the nth word / token from
	 *			the original String argument.
	 */
	public static String getArgumentN(String argument, int n, boolean all) {
            
        // A temporary holder String
        String keyArgument = "";
        // A StringTokenizer Object to go over the given String
        StringTokenizer st;
        // Our counter to find the nth word
        int tokenCount;
        // A boolean conditional to test against
        boolean stop = false;
        
        // Initialize our StringTokenizer
        st = new StringTokenizer(argument);
        // Start with a tokenCount of 0
        tokenCount = 0;

		// While there are more tokens and we haven't hit our nth word        
        while(st.hasMoreTokens() && tokenCount < n)  {
            
            // Let's keep going
            tokenCount++;
            keyArgument = st.nextToken();
        }
        
        // If there were less terms than we wanted
        if (tokenCount < n) {
            
            // We didn't find it
            return "";
        }
        
        // Otherwise, we return the nth word
        return keyArgument;
    }
}
