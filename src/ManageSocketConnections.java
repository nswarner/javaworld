/*	Purpose
 *
 *		The ManageSocketConnections class is meant to be a separate running thread from the
 *		main game thread. It accepts incoming connections and spawns a copy of itself when it's
 *		busy. It dies after it's accepted a single connection, but the copy, and by extension,
 *		its own copy (and the next copy, etc), lives on.
 *
 *	Algorithm
 *
 *		1. Declare the ManageSocketConnections class
 *		2. Import the necessary packages from IO and Net (files and sockets)
 *		3. Ensure our class implements Runnable
 *		4. Create a default constructor
 *		5. Create a paramterized constructor which accepts a Server Socket
 *		6. Create the run method
 *			- Accept a connection
 *			- Spawn a thread
 *			- Validate password credentials as needed
 *			- Setup either a new Player or load an existing player
 *		7. Create methods to get the player's name, their old password, or a new password
 *		8. Create a toString method
 *		9. Create an equals method
 *
 *	Structure / Process
 *
 *		We initialize a ServerSocket in the GameServer. A thread then begins which waits for the
 *		first incoming connection. When the first connection is accepted, a second thread is
 *		spawned which awaits the next connection while the first thread finishes up with the
 *		first player connected. The second thread in essence becomes the first thread, and will
 *		spawn its secondary thread once it accepts a connection.
 *
 *	Author			- Nicholas Warner
 *	Created 		- 4/24/2015
 *	Last Updated	- 5/1/2015
 */

// Import packages for file IO and Socket Networking
import java.io.*;
import java.net.*;

// Declare the class
public class ManageSocketConnections implements Runnable {

	// Declare our ServerSocket object
	ServerSocket server;

	// Default constructor
	public ManageSocketConnections() {
		
		this.server = null;
	}

	// Paramterized constructor with an isntantiated Server Socket
	public ManageSocketConnections(ServerSocket server) {
		
		this.server = server;
	}

	// The core of the Thread is the run method
	public void run() {
		
		// We'll take one Socket connection
		Socket oneConnection	= null;
		// We'll create one player from the Socket
		Player newPlayer		= null;
		// We'll use a PrintWriter for some Socket communication
		PrintWriter msgOut		= null;
		// We'll use a BufferedReader for some Socket communication
		BufferedReader msgIn	= null;
		
		// We'll pull this and associate it to the Player
		OutputStream playerOut	= null;
		// We'll pull this and associate it to the Player
		InputStream playerIn	= null;
		// The streams are important, if we use other techniques, they may block on read

		// A condition to loop until we've confirmed a name
		boolean confirmedName	= false;
		// A condition to loop until we've confirmed a password
		boolean confirmedPword	= false;
		
		// Strings for holding Socket input / temporary Strings
		String connectionInput	= "";
		String tmpHolder		= "";
		
		// Try to pull a single connection
		try {

			oneConnection = server.accept();
			
			// And spawn a new thread to wait for another connection
			new Thread(new ManageSocketConnections(server)).start();
			
			// Set the player's input and output channels right away
			playerOut	= oneConnection.getOutputStream();
			playerIn 	= oneConnection.getInputStream();
			
			// Set the preliminary method of communicating right away
			msgOut	= new PrintWriter(playerOut, true);
			msgIn	= new BufferedReader(new InputStreamReader(playerIn));
			
			// Send the new connection JavaWorld's Message of the Day!
			msgOut.println(Config.getMOTD());
			
		} // try
		
		// Catch any Exceptions
		catch (IOException e) {
			
			System.out.println("IOException caught in run: " + e.getMessage());
			System.exit(0);
		}

		// Instantiate our player		
		newPlayer = new Player();

		// Make sure we get a confirmed name
		while(!confirmedName) { 
			
			confirmedName = getPlayerName(msgOut, msgIn, newPlayer);
		}

		// Notify the game admin that someone's coming on
		System.out.println(newPlayer.getName() + " attempting to login.");

		// If it's an existing user
		if (Player.checkPlayerExists(newPlayer.getName())) {
			
			confirmedPword = false;

			// We need a confirmed password!			
			while(!confirmedPword) {

				confirmedPword = getPlayerPassword(msgOut, msgIn, newPlayer);
			}
			
			// If the password is good, we load them up!
			if (Player.checkPlayerLogin(newPlayer)) {
				
				// And make sure to clear the password
				newPlayer.clearPassword();
				
				// We synchronize for thread safety!
				synchronized(Player.class) {

					// Load the player		
					newPlayer = Player.loadPlayer(newPlayer.getName().toLowerCase());
					// Associate the player to their socket
					newPlayer.setPlayerConnection(oneConnection);
					// Set the output stream
					newPlayer.setOutput(playerOut);
					// Set the input stream
					newPlayer.setInput(playerIn);

					// If we just made a new seed, players haven't discovered any rooms yet!
					if (Config.getNewSeedFile()) {
						
						// Undiscover the rooms
						newPlayer.clearDiscoveredRooms();
					}

					// Add the player to the playerList
					Player.addPlayer(newPlayer);
				}

				// Show the new player their surroundings				
				Interpreter.checkCommand(newPlayer, "look"); 
			}
			
			// If their password was wrong
			else {
				
				// Notify them as such
				System.out.println("Incorrect password. Closing connection.");
				
				// Close it up
				try {

					oneConnection.close();
				}
				
				// Catch any Exceptions
				catch (IOException e) {
					
					System.out.println("IOException in login Thread: " + e.getMessage());
				}
				 
				// Thread can now die
				return;
			}
		}
		
		// We have a new user!
		else {
			
			confirmedPword = false;
			
			// Confirm the password
			while(!confirmedPword) {

				confirmedPword = getNewPlayerPassword(msgOut, msgIn, newPlayer);
			}
			
			// Create a new file with their password
			Player.createPWordFile(newPlayer);
			// Save their new profile!
			Player.savePlayer(newPlayer);
			// Clear their password
			newPlayer.clearPassword();
			// Initialize the Socket stuff
			newPlayer.setPlayerConnection(oneConnection);
			newPlayer.setOutput(playerOut);
			newPlayer.setInput(playerIn);
			// Place them into the HOMELOCATION
			newPlayer.setCurrentRoom(World.getRoom(100, 100));
			// Make sure they're in JavaWorld!
			newPlayer.setCurrentWorld(World.getRoom(100, 100).getWorld());
			// And they just discovered (100, 100)!
			newPlayer.discoverRoom();
			
			// Synchronize for thread safety
			synchronized(Player.class) {
				
				// Add the player
				Player.addPlayer(newPlayer);
			}

			// Let them see their surroundings
			Interpreter.checkCommand(newPlayer, "look");
		}

		// Notify the game admin that somebody new is in the game
		System.out.println("Added one Player: " + newPlayer.getName());		
	}
	
	// A method which gets the player's name
	private boolean getPlayerName(PrintWriter msgOut, BufferedReader msgIn, 
								  Player newPlayer) {

		// String holders for input
		String connectionInput	= "";
		String tmpName			= "";
		
		// Ask the user to input their name
		msgOut.print("Please input your name: ");
		// print doesn't force a flush so we manually flush the buffer
		msgOut.flush();

		// Try to read in a line; we actually block here
		try {

			tmpName = msgIn.readLine();
		}
		
		// Catch any Exceptions
		catch (IOException e) {
			
			System.out.println("IOException in getPlayerName: " + e.getMessage());
		}

		// Unfortunately, we don't allow multiple names		
		if (tmpName.indexOf(' ') > -1) {

			// So we only take the first name			
			tmpName = tmpName.substring(0, tmpName.indexOf(' '));
		}

		// If the player exists, we're good to go!
		if (Player.checkPlayerExists(tmpName.toLowerCase())) {
			
			tmpName = tmpName.toUpperCase().charAt(0) + tmpName.substring(1);
			newPlayer.setName(tmpName);
			return true;
		}
		
		// If not, we begin the new player creation process
		msgOut.print("\nIs " + tmpName + " okay as a name (y/n)? ");
		// Flush again
		msgOut.flush();
		
		// Try again, blocking
		try {

			connectionInput = msgIn.readLine().trim().toLowerCase();
		}
		
		// Catch any Exceptions
		catch (IOException e) {
			
			System.out.println("IOException caught in getPlayerName: " + e.getMessage());
		}
		
		// if they want the name, that's great
		if (connectionInput.length() > 0 && connectionInput.charAt(0) == 'y') {
			
			tmpName = tmpName.toUpperCase().charAt(0) + tmpName.substring(1);
			newPlayer.setName(tmpName);
			msgOut.println("Player name set to " + tmpName + ".");
			return true;
		}
		
		// But if they don't, the process starts again
		return false;
	}

	// A method to request a password
	private boolean getPlayerPassword(PrintWriter msgOut, BufferedReader msgIn, 
									  Player newPlayer) {

		// The password being input
		String tempPassword = "";
		
		// Prompt the player to input their password
		msgOut.print("Please input your Password: ");
		// Flush because print doesn't flush the buffer
		msgOut.flush();

		// Try to read something; blocking
		try {

			tempPassword = msgIn.readLine();
		}

		// Catch Exceptions		
		catch (IOException e) {
			
			System.out.println("IOException in getPlayerPassword: " + e.getMessage());
		}
		
		// We have a password! Set it up!
		newPlayer.setPassword(tempPassword);
		
		// Good to go
		return true;
	}	

	// Let's prompt a new player for their password twice
	private boolean getNewPlayerPassword(PrintWriter msgOut, BufferedReader msgIn, 
									  Player newPlayer) {

		// We need two copies of it to make sure they're equal
		String[] tempPassword = new String[2];
		
		// So let's get one
		msgOut.print("Please input your new Password: ");
		// print doesn't flush buffer so we manually flush
		msgOut.flush();

		// Try to read a line; blocking
		try {

			tempPassword[0] = msgIn.readLine();
		}
		
		// Catch an Exception
		catch (IOException e) {
			
			System.out.println("IOException in getPlayerPassword[0]: " + e.getMessage());
		}

		// Repeat the process		
		msgOut.print("Please confirm your new Password: ");
		msgOut.flush();
		
		// Try to read a line; blocking
		try {

			tempPassword[1] = msgIn.readLine();
		}
		
		// Catch an Exception
		catch (IOException e) {
			
			System.out.println("IOException in getPlayerPassword[1]: " + e.getMessage());
		}
		
		// If we have a match
		if (tempPassword[0].equals(tempPassword[1])) {
			
			// Set the password, good to go
			newPlayer.setPassword(tempPassword[0]);
			// Notify the player
			msgOut.println("Player's password was set.");
			return true;
		}

		// They need to try again!
		else {
			
			msgOut.println("Player's passwords did not match. Please try again.");
		}		

		return false;
	}	
	
	// toString method
	public String toString() {
		
		return "Class: ManageSocketConnections";
	}
	
	// equals method
	public boolean equals(ManageSocketConnections msg) {
		
		if (toString().equals(msg.toString())) {
			
			return true;
		}
		
		return false;
	}
}