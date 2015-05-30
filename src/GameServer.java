/*	Purpose
 *
 *		The GameServer class houses the initialization and core processes of JavaWorld. The first
 *		steps include initializing the server for incoming network connections. The next steps
 *		include loading all Zones, Rooms, Mobs, Items, and other chunks from the Database. Then
 *		we spawn the first Thread which manages incoming connections. At this point, we then
 *		begin the primary game loop where we interpret the incoming commands from each connection.
 *
 *	Algorithm
 *
 *		1. Declare the GameServer class
 *		2. Import Network and IO packages
 *		3. Import the Scanner class
 *		4. Declare the main method
 *		5. Declare and instantiate a Scanner object for keyboard input
 *		6. Declare Strings for temporary file and user input
 *		7. Declare a boolean for the main game loop
 *		8. Declare and instantiate the main World object
 *		9. Declare a ServerSocket object
 *		10. Try to initialize the ServerSocket object using the constant Port
 *		11. Catch any Exceptions
 *		12. Load all of the default rooms into our World object
 *		13. Load all of the default things 
 *		14. Ask the user if they want to use the default config file
 *		15. If the user doesn't explicitly say no, load the default config file
			- Call the Config and Seed initialization methods
 *		16. Load the pseudo-random double generator from file (considered our seed)
 *		17. Notify the admin that the game has finished loading
 *		18. Spawn a thread to accept incoming connections
 *		19. Begin the main game loop
 *			- Check if there are any things to update
 *			- Synchronize the Player class to ensure thread safety while using
 *				the Player's Vector
 *				- Update the state of all players
 *				- Check if any player has new input
 *			- Yield the thread to allow for the login thread to add a new player
 *
 *	Structure / Process
 *
 *		After the ServerSocket is initialized, the Zones are loaded first in the hierarchy. Rooms
 *		are then loaded, as they are substructures of a zone, so multiple rooms share a single 
 *		zone. After Zones and Rooms, Items are loaded which may be lying about the rooms. Then
 *		the Mobs are loaded which exist within a room in a zone and which may have Items in their
 *		Inventory. This is the initial load. 
 *	
 *	Author			- Nicholas Warner
 *	Created			- 4/24/2015
 *	Last Updated	- 5/1/2015
 */

// Import necessary packages, classes, and interfaces
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class GameServer {

	// main method
	public static void main(String[] args) {

		// Keyboard input for dynamic settings (Admin etc)
		Scanner keyboard	= new Scanner(System.in);

		// To store user console input		
		String userInput	= "";
		
		// To store the file input
		String fileInput	= "";

		// boolean to control the main game loop
		boolean gameLoop	= true;

		// First zone we load in
		World defaultZone	= new World("JavaWorld");
		
		// ServerSocket object
		ServerSocket server	= null;

		// Initializing the ServerSocket object
		try {
			
			// Ensure it binds to the given Config PORT
			server = new ServerSocket(Config.PORT);
			
			// If the program terminates abnormally, allow the port to be reused immediately
			server.setReuseAddress(true);
		}

		// Catch any given Exceptions
		catch (IOException e) {
			
			// Notify the Administrator of the Game
			System.out.println("Issue with server side socket initialization: " + 
								e.getMessage() + "\nExiting...");
			System.exit(0);
		}

		// Load our default zone
		defaultZone.loadRooms();
		
		// See if they want to generate a new Seed
		Seed.promptForSeed();
		
		// Use the default config
		Config.loadDefaultFile();

		// If we don't want to use the default admin
		// Config.promptForAdmin();

		// Load up our fixed pseudo-random double generator
		Seed.loadRandomNumbers();

		// Load the dynamic building names and descriptions
		Room.loadDynamicBuildingValues();

		// Dynamically load the world, arbitrary number of rooms
		defaultZone.dynamicallyBuildWorld(500);
		
		// Dynamically populate the world with items
		Item.dynamicallyBuildItems(1, -1, -1, true);

		// Notify the admin of successful load
		System.out.println("JavaWorld ready to rock on port " + Config.PORT);
		
		// Create a separate thread for connection management
		(new Thread(new ManageSocketConnections(server))).start();

		// Main Game Loop
		while(gameLoop) {

			// To conform to the requirements / limitations of the project
			synchronized(Player.class) {

				// Update the Player's general state (thirst, hunger, etc)
				Player.updateState();
				// Check if they have any commands waiting
				Player.interpretInput();				
			}

			// Not an ideal solution; ensures that incoming connections are added quickly
			Thread.yield();
			
			// Avoid excessive CPU use, 10 loops per second max
			ToolKit.slow(10);

		} // gameLoop
		
	} // main
}