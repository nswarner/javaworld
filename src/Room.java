/*	Purpose
 *
 *		The Room class is a representation of a single unit of 2D space. Any number of players
 *		can occupy this unit of space. Rooms can be of multiple environmental types like caves,
 *		forests, parks, cities, etc. 
 *
 *	Algorithm
 *
 *		1. Declare the room class
 *		2. Import the necessary packages, classes, interfaces, ...
 * 		3. Declare a constant String Array which represent the filesystem location of each 
 *			of the different room data files
 *			- Cave, City, Forest, Lake, Mountain, Park
 *		4. Declare constant ints which represent the different room filenames in the
 *			given String array, i.e. int CAVE represents index [5]
 *		5. Declare an ArrayList for each of the different roomtype room names
 *			- forestRoomNames, lakeRoomNames, ...
 *		6. Declare an ArrayList for each of the different roomtype room descriptions
 *			- forestRoomDesc, lakeRoomDesc, ...
 *		7. Declare private instance variables to hold room fields
 *			- int roomX, int roomY -> (x, y) location of the room
 *			- String roomName -> Name of the room
 *			- String roomDescription -> Description of the room
 *			- World insideWorld -> A reference to the World Object the room exists within
 *			- boolean newRoom -> A flag indicating whether this room is new or not
 *			- Inventory roomInventory -> An inventory object which collects the number of
 *				items within the room and allows it to be listed
 *		8. Declare a default constructor
 *		9. Declare parameterized constructors
 *			- If the (x, y), name, desc, and World are given
 *			- If the (x, y), name, desc, World, and a boolean for new room are given
 *		10. Declare necessary accessor methods
 *			- Create a getExits method for a given (x, y) room
 *			- Create a getInventory method which calls the Inventory Object's
 *				displayInventory method
 *		11. Declare necessary mutator methods
 *			- Create a method to set and remove the new room flag
 *			- Create a method to remove an item from the room's inventory by calling
 *				the inventory's removeFromInventory method
 *		12. Declare a method to test whether there are players in a given room
 *			- Call Player.anyPlayersInRoom()
 *		13. Declare a static method to load the dynamically generated room values
 *			- One call for each room type (FOREST, LAKE, CITY, ...)
 *		14. Declare a static method to dynamically load one specific room type
 *			- Declare and instantiate a Scanner object
 *				- Read in until -1, this is the roomName section
 *				- Read in until -2, this is the roomDesc section
 *			- CLOSE THE FILE
 *			- Catch any Exceptions
 *		15. Declare a method to get a random room name from a specified room ArrayList
 *		16. Declare a method to get a random room desc from a specified room ArrayList
 *		17. Declare a method to test whether a room contains a given item or not
 *		18. Declare a toString method
 *		19. Declare an equals method
 *
 *	Structure / Process
 *
 *		Rooms are loaded in via the World class initially. So the hierarchy goes something like
 *		1 World -> Many Rooms -> Many Many Players / Items / Things.
 *
 *	Author			- Nicholas Warner
 *	Created 		- 4/24/2015
 *	Last Updated	- 5/1/2015
 */

// Import the necessary classes, packages, interfaces, ...
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

// Declare the room class
public class Room {

	// Declare a constant String Array for filenames of the different room types
	private static final String[] rTypeFilenames	= { "../data/forestTemplate.data",
														"../data/lakeTemplate.data",
														"../data/cityTemplate.data",
														"../data/parkTemplate.data",
														"../data/mountainTemplate.data",
														"../data/caveTemplate.data" };

	// Declare final ints which represent the different indices of the filename Array
	private static final int FOREST		= 0;
	private static final int LAKE		= 1;
	private static final int CITY		= 2;
	private static final int PARK		= 3;
	private static final int MOUNTAIN	= 4;
	private static final int CAVE		= 5;
	
	// Declare ArrayLists which contain the roomNames of each room type
	private static ArrayList<String> forestRoomNames	= new ArrayList<String>();
	private static ArrayList<String> lakeRoomNames		= new ArrayList<String>();
	private static ArrayList<String> cityRoomNames		= new ArrayList<String>();
	private static ArrayList<String> parkRoomNames		= new ArrayList<String>();
	private static ArrayList<String> mountainRoomNames	= new ArrayList<String>();
	private static ArrayList<String> caveRoomNames 		= new ArrayList<String>();

	// Declare ArrayLists which contain the roomDescs of each room type
	private static ArrayList<String> forestRoomDesc		= new ArrayList<String>();
	private static ArrayList<String> lakeRoomDesc		= new ArrayList<String>();
	private static ArrayList<String> cityRoomDesc		= new ArrayList<String>();
	private static ArrayList<String> parkRoomDesc		= new ArrayList<String>();
	private static ArrayList<String> mountainRoomDesc	= new ArrayList<String>();
	private static ArrayList<String> caveRoomDesc		= new ArrayList<String>();
	
	// Declare ints which represent the coordinates of the room
	private int roomX;
	private int roomY;
	// Declare a String for the name of the room
	private String roomName;
	// Declare a String for the description of the room
	private String roomDescription;
	// Declare a World object to be a reference for the main Java World Object
	private World insideWorld;
	// Declare a boolean for flagging if it's a new room or not
	private boolean newRoom;
	// Declare an Inventory object to hold the room's Inventory
	private Inventory roomInventory;
	
	// Default constructor
	public Room() {
		
		// The room's X coordinate
		roomX			= -1;
		// The room's Y coordinate
		roomY			= -1;
		// The room's name
		roomName		= "";
		// The room's description
		roomDescription	= "";
		// A reference to the main World object
		insideWorld		= World.getWorld();
		// The new room flag, not a new room
		newRoom			= false;
		// The room's inventory
		roomInventory	= new Inventory(true);
	}

	// A parameterized constructor but not a new room	
	public Room(int rX, int rY, String name, String desc, World world) {
		
		// The room's X coordinate
		roomX			= rX;
		// The room's Y coordinate
		roomY			= rY;
		// The room's name
		roomName		= name;
		// The room's description
		roomDescription	= desc;
		// A reference to the main World object
		insideWorld		= world;
		// The new room flag, not a new room
		newRoom			= false;
		// The room's inventory
		roomInventory	= new Inventory(true);
	}
	
	// A parameterized constructor for a new room
	public Room(int rX, int rY, String name, String desc, World world, boolean nRoom) {
		
		// The room's X coordinate
		roomX			= rX;
		// The room's Y coordinate
		roomY			= rY;
		// The room's name
		roomName		= name;
		// The room's description
		roomDescription	= desc;
		// A reference to the main World object
		insideWorld		= world;
		// The new room flag, this is a new room
		newRoom			= nRoom;
		// The room's inventory
		roomInventory	= new Inventory(true);
	}

	// Accessor Methods
    public boolean getNewRoomFlag() {
		
		return newRoom;
	}

	// A method to get the exits as a String
    public String getExits(int x, int y) {
		
		// The String we'll return
		String tmp = "Exits: ";
		// A boolean to test whether there was an exit or not
		boolean foundExit = false;
		
		// If a room exists to the east
		if (World.checkRoomExists(x + 1, y)) {
			
			// The east exit exists
			tmp += "[East]";
			// We found an exit
			foundExit = true;
		}

		// If a room exists to the west		
		if (World.checkRoomExists(x - 1, y)) {
			
			// The west exit exists
			tmp += "[West]";
			// We found an exit
			foundExit = true;
		}

		// If the room exists to the north
		if (World.checkRoomExists(x, y + 1)) {
			
			// The north exit exists
			tmp += "[North]";
			// We found an exit
			foundExit = true;
		}
		
		//  If the room exists to the south
		if (World.checkRoomExists(x, y - 1)) {
			
			// The south exit exists
			tmp += "[South]";
			// We found an exit
			foundExit = true;
		}
		
		// We didn't find an exit
		if (!foundExit) {
			
			// So we notify them that no exits exist.
			tmp += "#rNone.#n";
		}
		
		// Send the built String back
		return (tmp + "\n");
	}
    
	public String getRoomName() {
		
		return roomName;
	}
   
	public String getRoomDescription() {
		
		return roomDescription;
	}

	public World getWorld() {
		
		return insideWorld;
	}
  
	public int getX() {
		
		return roomX;
	}

    public int getY() {
		
		return roomY;
	}

	// A method to get a String which displays the room's inventory
    public String getInventory() {
    	
    	return roomInventory.displayInventory(true);
    }

	// Mutator Methods
	// A method to set the new flag (or other flags)
	public void setFlag(String flag) {
		
		// Make sure it's a good String
		flag = flag.toLowerCase().trim();
		
		// And check it
		switch(flag) {
			
			// And it's new so let's set it
			case "new": this.newRoom = true; break;
			// Didn't work
			default: break;
		}
	}

	// A method to remove the new flag (or other flags)
    public void removeFlag(String flag) {
		
		// Make sure it's a good String
		flag = flag.toLowerCase().trim();
		
		// And check it
		switch(flag) {
			
			// And it's new so let's remove it
			case "new": this.newRoom = false; break;
			// Didn't work
			default: break;
		}
	}
	
    public void setName(String name) {
		
		roomName = name;
	}
	
    public void setDescription(String description) {
		
		roomDescription = description;
	}
	
    public void addItem(Item item) {
		
		roomInventory.addToInventory(item);
	}
	
	public void removeItem(Item item) {
		
		roomInventory.removeFromInventory(item);
	}

	// Room Methods
	// Test whether there are any players in the room  
    public boolean playersInRoom() {

		// Call the Player anyPlayersInRoom method to test
		return Player.anyPlayersInRoom(roomX, roomY);
    }

	// A method to load each of the dynamic room's values into their ArrayList
    public static void loadDynamicBuildingValues() {
    	
    	loadDynamicRoomValues(FOREST);
    	loadDynamicRoomValues(LAKE);
    	loadDynamicRoomValues(CITY);
    	loadDynamicRoomValues(PARK);
    	loadDynamicRoomValues(MOUNTAIN);
    	loadDynamicRoomValues(CAVE);
    }
    
    // A method to load a specific dynamic room type's values into its ArrayList
    public static void loadDynamicRoomValues(int rType) {
    	
    	// Make sure we have the right fileName
    	String fileName		= rTypeFilenames[rType];
    	// A temporary String to hold fileInput
    	String fileInput	= "";
    	
    	// Let's try to read it in
    	try {
    		
    		// Declare and instantiate a Scanner object
    		Scanner fileIn = new Scanner(new File(fileName));
    		
    		// Read in the room name section (-1 indicates section is finished)
    		while(!(fileInput = fileIn.nextLine()).equals("-1")) {
    			
    			switch(rType) {
    				
    				case FOREST:	forestRoomNames.add(fileInput); break;
    				case LAKE:		lakeRoomNames.add(fileInput); break;
    				case CITY:		cityRoomNames.add(fileInput); break;
    				case PARK:		parkRoomNames.add(fileInput); break;
    				case MOUNTAIN:	mountainRoomNames.add(fileInput); break;
    				case CAVE:		caveRoomNames.add(fileInput); break;
    				default: break;
    			}
    		}
    		
    		// Read in the room description section (-2 indicates section is finished)
    		while(!(fileInput = fileIn.nextLine()).equals("-2")) {
    			
    			switch(rType) {
    				
    				case FOREST:	forestRoomDesc.add(fileInput); break;
    				case LAKE:		lakeRoomDesc.add(fileInput); break;
    				case CITY:		cityRoomDesc.add(fileInput); break;
    				case PARK:		parkRoomDesc.add(fileInput); break;
    				case MOUNTAIN:	mountainRoomDesc.add(fileInput); break;
    				case CAVE:		caveRoomDesc.add(fileInput); break;
    				default: break;
    			}    			
    		}
    		
    		// Always close the file!
    		fileIn.close();
    	}
    	
    	// Catch any Exceptions
    	catch (IOException e) {
    		
    		System.out.println("Exception in loadRoomNames: " + e.getMessage());
    		System.exit(0);
    	}
    }
    
    // Declare a method to get a random room name from a specified room ArrayList
    public static String getBuildName(int rType) {
    	
    	String returnValue	= "";
    	int randomPosition	= 0; 
    	
    	switch(rType) {
    		
			case FOREST:	randomPosition	= (int)(Seed.rand() * forestRoomNames.size());
							returnValue		= forestRoomNames.get(randomPosition);
							break;
			case LAKE:		randomPosition	= (int)(Seed.rand() * lakeRoomNames.size());
							returnValue		= lakeRoomNames.get(randomPosition);
							break;
			case CITY:		randomPosition	= (int)(Seed.rand() * cityRoomNames.size());
							returnValue		= cityRoomNames.get(randomPosition);
							break;
			case PARK:		randomPosition	= (int)(Seed.rand() * parkRoomNames.size());
							returnValue		= parkRoomNames.get(randomPosition);
							break;
			case MOUNTAIN:	randomPosition	= (int)(Seed.rand() * mountainRoomNames.size());
							returnValue		= mountainRoomNames.get(randomPosition);
							break;
			case CAVE:		randomPosition	= (int)(Seed.rand() * caveRoomNames.size());
							returnValue		= caveRoomNames.get(randomPosition);
							break;
			default: break;    		
    	}
    	
    	return returnValue;
    }
    
    // Declare a method tog et a random room description from a specified room ArrayList
    public static String getBuildDesc(int rType) {
    	
    	String returnValue	= "";
    	int randomPosition	= 0; 
    	
    	switch(rType) {
    		
			case FOREST:	randomPosition	= (int)(Seed.rand() * forestRoomDesc.size());
							returnValue		= forestRoomDesc.get(randomPosition);
							break;
			case LAKE:		randomPosition	= (int)(Seed.rand() * lakeRoomDesc.size());
							returnValue		= lakeRoomDesc.get(randomPosition);
							break;
			case CITY:		randomPosition	= (int)(Seed.rand() * cityRoomDesc.size());
							returnValue		= cityRoomDesc.get(randomPosition);
							break;
			case PARK:		randomPosition	= (int)(Seed.rand() * parkRoomDesc.size());
							returnValue		= parkRoomDesc.get(randomPosition);
							break;
			case MOUNTAIN:	randomPosition	= (int)(Seed.rand() * mountainRoomDesc.size());
							returnValue		= mountainRoomDesc.get(randomPosition);
							break;
			case CAVE:		randomPosition	= (int)(Seed.rand() * caveRoomDesc.size());
							returnValue		= caveRoomDesc.get(randomPosition);
							break;
			default: break;    		
    	}
    	
    	return returnValue;
    }
    
    // A method to test whether the room contains a given item
    public Item containsItem(String oneItem) {
    	
    	return roomInventory.contains(oneItem);
    }

	// toString method    
    public String toString() {
    	
    	return "Class: Room\n\rName: " + roomName + "LocationX: " + roomX + "\n\rLocationY: " + 
    		   roomY + "\n\rRoomDescription: " + roomDescription + "\n\r";
    }
    
    // equals Method
    public boolean equals(Room oneRoom) {
    	
    	if (toString().equals(oneRoom.toString())) {
    		
    		return true;
    	}
    	
    	return false;
    }
}