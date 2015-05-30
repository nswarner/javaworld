/**
 *	Purpose
 *
 *		The World is a class that constitutes the overall physcial JavaWorld game. It contains a
 *		list of all rooms, and is part of the initial startup for loading rooms and generating
 *		dynamic rooms.
 *
 *	Algorithm
 *
 *		1. Declare the World Class
 *		2. Import the necessary packages, classes, interfaces, ...
 *		3. Declare a constant String to reference the primary Zone file path
 *		4. Declare a constant int which reflects the maximum number of rooms to generate
 *		5. Declare a constant int to reflect the "home" location of every player
 *			- Generally, this should always be (100, 100)
 *		6. Declare four constant ints to reflect the directions of north, south, east,
 *			and west, referenced via an array's index
 *		7. Declare six constant ints to cover the different room types
 *			- FOREST, LAKE, CITY, PARK, CAVE, MOUNTAIN
 *		8. Declare an array of room types (to go with 7)
 *		9. Declare a constant for the maximum number of rooms to be queued up to be
 *			built
 *		10. Declare a static 2D array to represent the World (x, y)
 *		11. Declare a static reference to the instantiated World
 *			- With a little effort, the world may be extended into multiple worlds
 *		12. Declare a String to represent the name of the world "Java World"
 *		13. Declare an ArrayList to represent the "soon to be built" rooms
 *		14. Declare an int to represent where we are in building new rooms
 *		15. Declare a boolean as a condition for when we're supposed to build new rooms and
 *			when we've built too many new rooms
 *		16. Declare a default constructor
 *		17. Declare a parameterized constructor
 *		18. Declare any necessary Accessor methods
 *		19. Declare a method to add a room to the World
 *		20. Declare a method to check if a given (x, y) room exists
 *		21. Declare a method to load all rooms, starting with the static rooms from the
 *			zone file and then loading the dynamic rooms
 *		22. Declare a method which is used to dynamically generate the World
 *		23. Declare a method to build a single room at a given (x, y)
 *		24. Declare a method to figure out which directions are possible to build a room in
 *		25. Declare a method to add another dynamic room
 *		26. Declare a toString method
 *		27. Declare an equals method
 *	Structure / Process
 *
 *		In the GameServer class, the World class is used to initially load the HOMELOCATION and it's
 *		surrounding rooms from a flat file. Then the World class is called to dynamically generate
 *		the surrounding world using the pseudo-random Seed class.
 *
 *	Author			- Nicholas Warner
 *	Created 		- 4/24/2015
 *	Last Updated	- 4/30/2015
 */

// Import our packages
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileOutputStream;

// The World Class
public class World {
	
	// A few constants; the starting zone flat file
    public static final String ZONEFILE		= "../zones/StartingZone.zone";
	// The maximum number of x rooms and y rooms (MAXROOMS^2 for max number of rooms)
    public static final int MAXROOMS		= 200;
    // The home location of (100, 100), safety!
	public static final int HOMELOCATION   	= 100;

	// Directions relating to movement
	private final int NORTH	= 0;
	private final int EAST	= 1;
	private final int SOUTH	= 2;
	private final int WEST	= 3;

	// Different area types
	private final int FOREST	= 0;
	private final int LAKE		= 1;
	private final int CITY		= 2;
	private final int PARK		= 3;
	private final int CAVE		= 4;
	private final int MOUNTAIN	= 5;

	// A String array of the different area types
	private final String[] roomTypes	= { "Forest", "Lake", "City", "Park", "Cave", "Mountain" };

	// 200 here is arbitrary; we use this to know how to build our world
	private final int MAX_QUEUED_ROOMS	= 200;

	// This is the actual "world"; a 2D array of Rooms
	private static Room[][] rooms = new Room[MAXROOMS][MAXROOMS];

	// The World is the world is the world
	private static World world = null;
	// The name of the world; JavaWorld!
	private String worldName;
	// An arrayList for rooms which are going to be dynamically generated soon
	private ArrayList<Coordinate> newRoomList;
	// Part of the newRoomList
	private int buildingNextRoom;
	// Knowing where we've already built
	private int builtNextRoom;
	// And when to stop adding rooms
	private boolean stopAddingRooms;

	// Our default constructor	
	public World() {
		
		worldName			= "";
		world				= this;
		newRoomList			= new ArrayList<Coordinate>();
		
		buildingNextRoom	= 0;
		builtNextRoom		= 0;
		stopAddingRooms		= false;
	}
	
	// And paramterized constructor
	public World(String name) {
		
		worldName			= name;
		world				= this;
		newRoomList			= new ArrayList<Coordinate>();
		
		buildingNextRoom	= 0;
		builtNextRoom		= 0;
		stopAddingRooms		= false;
	}

	// Accessor Methods
	public static World getWorld() {
		
		return world;
	}
	
	// Return a single room
	public static Room getRoom(int x, int y) {
		
		// Make sure with bounds checking
		if (x < MAXROOMS && y < MAXROOMS && x >= 0 && y >= 0) {

			if (rooms[x][y] != null) {

				return rooms[x][y];
			}
		}
		
		return null;
	}

	public String getWorldName() {
		
		return worldName;
	}

	// Return the coordiantes of the next room we'll dynamically build
	private Coordinate getNextRoom() {

		Coordinate temp = null;
		
		// If we already have stuff to pull
		if (newRoomList.size() > 0) {
		
			temp = newRoomList.get(0);

			// There is some ArrayList overhead here, but not terribly much
			newRoomList.remove(0);
		}

		// If we've exhausted the list		
		else if (newRoomList.size() == 0 && stopAddingRooms) {
			
			// We start adding again
			stopAddingRooms = false;

			// We try and find a different direction to start from
			if (World.getRoom(100, 102) == null) {
			
				temp = new Coordinate(100, 102);
			}
			
			else if (World.getRoom(102, 100) == null) {
				
				temp = new Coordinate(102, 100);
			}
			
			else if (World.getRoom(100, 98) == null) {
				
				temp = new Coordinate(100, 98);
			}
			
			else if (World.getRoom(98, 100) == null) {
				
				temp = new Coordinate(98, 100);
			}			
		}
		
		// And pass back the coordinates
		return temp;
	}

	// World Methods
	// Let's add a given room to the specified (x, y)
	public static void addRoom(Room newRoom, int x, int y) {
	
		// If the bounds are checked
		if (x >= 0 && x < MAXROOMS && y >= 0 && y < MAXROOMS) {
	
			// And the room doesn't exist
			if (!checkRoomExists(x, y)) {
	
				// Then it does now!
				rooms[x][y] = newRoom;
				
				// And if we're generous
				if (ToolKit.rand(0, 20) > 15) {

					// The room get's a random item!
					Item.dynamicallyBuildItems(1, x, y, false);
				}
			}
		}
	}

	// Test whether a room exists or not
    public static boolean checkRoomExists(int x, int y) {

		// Bounds check
        if (x < MAXROOMS && x >= 0 && y < MAXROOMS && y >= 0) {

			// It does, we're good
            if (rooms[x][y] != null) {
            
                return true;
            }
        }
        
        // It doesn't or bounds are bad
        return false;
    }

	// Let's load up the home zone
	public void loadRooms() {

		// Has an x and y
		int roomX = 0;
		int roomY = 0;
		// A name and Description
		String roomName = "";
		String roomDesc = "";
		// Our temporary room
		Room oneRoom = null;
		// And file reader
		Scanner fileIn = null;
		
		// Let's try to open it
		try {
			
			// Looks good
			fileIn = new Scanner(new FileInputStream("../zones/StartingZone.zone"));
		}
		
		// Catch an Exception
		catch (FileNotFoundException e) {
			
			System.out.println("StartingZone.zone not found. Exiting.");
			System.exit(0);
		}

		// Read it up while there's still lines		
		while(fileIn.hasNextLine()) {

			// Read in one Room
			roomX		= fileIn.nextInt();
			
			// If we're not at the end of the file, -1 EOF
			if (roomX != -1) {
			
				// We get our roomy stuff
				roomY		= fileIn.nextInt();
				roomName	= fileIn.nextLine().substring(1); // An extra space after the int
				roomDesc	= fileIn.nextLine();
				
				// And create a room
				oneRoom = new Room(roomX, roomY, roomName, roomDesc, this);
				// And add it to our room list
				this.addRoom(oneRoom, roomX, roomY);
			}
		}

		// Always close the file		
		fileIn.close();
		
		// Notify the game admin
		System.out.println("Rooms loaded successfully.");
	}

	// And the method where we dynamically build the world
	public void dynamicallyBuildWorld(int sizeOfWorld) {
		
		String fromDirection = "";
		// Home Location
		int locationX = World.HOMELOCATION;
		int locationY = World.HOMELOCATION;
		// Default starting area type
		int rType	  = FOREST;
		Coordinate temp	= null;
		
		// Every room can have up to 4 exits; north, south, east, west
		boolean[] roomExists	= new boolean[4];

		if (sizeOfWorld <= 0) {
			
			sizeOfWorld = MAXROOMS; // only 200
		}
		
		for(int i = 0;i < 4; i++) {
			
			roomExists[i] = false;
		}
		
		// Figure out which direction we begin in (from a 3x3)
		switch((int)Seed.rand() * 4) {
			
			case 0: locationX += 2; break;
			case 1: locationX -= 2; break;
			case 2: locationY += 2; break;
			case 3: locationY -= 2; break;
			default:
				System.out.println("dynamicallyBuildWorld switch was default! Oops.");
				locationX += 2;
				break;
		}
		
		// Notify the game admin we've started building
		System.out.println("Dynamically building world from " + Config.getSeedFileName() + ".");
		
		// Build as many rooms as requested
		for(int i = 0; i < sizeOfWorld; i++) {
			
			// Check which exits are okay
			buildExitCheck(roomExists, locationX, locationY);
			
			// If north is okay and we rolled randomly 50%+
			if (!roomExists[NORTH] && (int)(Seed.rand() * 20) < 10) {

				// 3% chance to change the area type from Forest to Park/City/Lake...
				if (Seed.rand() * 100 < 3) {
					
					rType = (int)Seed.rand() * 6;
				}
				
				// Build the room and add it
				addRoom(buildRoom(locationX, locationY + 1, rType), locationX, locationY + 1);

				// And the next room we go to is north to build from and check exits
				addNextRoom(locationX, locationY + 1);
			}
			
			// Repeat north comments
			if (!roomExists[EAST] && (int)(Seed.rand() * 20) < 10) {

				if (Seed.rand() * 100 < 3) {
					
					rType = (int)Seed.rand() * 6;
				}
				
				addRoom(buildRoom(locationX + 1, locationY, rType), locationX + 1, locationY);

				addNextRoom(locationX + 1, locationY);
			}

			// Repeat north comments
			if (!roomExists[SOUTH] && (int)(Seed.rand() * 20) < 11) {

				if (Seed.rand() * 100 < 3) {
					
					rType = (int)Seed.rand() * 6;
				}
				
				addRoom(buildRoom(locationX, locationY - 1, rType), locationX, locationY - 1);

				addNextRoom(locationX, locationY - 1);
			}
			
			// Repeat north comments
			if (!roomExists[WEST] && (int)(Seed.rand() * 20) < 11) {
				
				if (Seed.rand() * 100 < 3) {
					
					rType = (int)Seed.rand() * 6;
				}

				addRoom(buildRoom(locationX - 1, locationY, rType), locationX - 1, locationY);
				
				addNextRoom(locationX - 1, locationY);
			}

			// And this next room is where we look to next
			temp = getNextRoom();

			// Pending it's not null
			if (temp != null) {

				// We have new X and Y coordinates
				locationX = temp.getCoordX();
				locationY = temp.getCoordY();
			}
			
			// Otherwise, the seed might've been bad. Let's try again!
			else {
				
				System.out.println("Dynamic Building Code did not build " + sizeOfWorld +
								   " rooms. This seed may be bad. Consider restarting and " +
								   "reseeding. (Only " + i + " rooms were built.");
				
				// We end
				break;
			}
		}
	}
	
	// Build a room given two coordinates and a room type
	private Room buildRoom(int locationX, int locationY, int rType) {
	
		// Make sure to bounds check
		if (locationX >= 0 && locationX < MAXROOMS && locationY >= 0 && locationY < MAXROOMS) {
		
			// Build the room and return it
			return (new Room(locationX, locationY, Room.getBuildName(rType), 
							 Room.getBuildDesc(rType), World.getWorld(), true));
		}
		
		// No room, failed bounds check
		return null;
	}
	
	// Check the exits to see which directions have exits and which don't
	private void buildExitCheck(boolean[] roomExists, int roomX, int roomY) {
		
		if (World.checkRoomExists(roomX + 1, roomY)) {
			
			roomExists[EAST] = true;
		}
		
		else {
			
			roomExists[EAST] = false;
		}
		
		if (World.checkRoomExists(roomX - 1, roomY)) {
			
			roomExists[WEST] = true;
		}
		
		else {
			
			roomExists[WEST] = false;
		}
		
		if (World.checkRoomExists(roomX, roomY + 1)) {
			
			roomExists[NORTH] = true;
		}
		
		else {
			
			roomExists[NORTH] = false;
		}
		
		if (World.checkRoomExists(roomX, roomY - 1)) {
			
			roomExists[SOUTH] = true;
		}
		
		else {
			
			roomExists[SOUTH] = false;
		}
	}
	
	// Add a coordinate object for x, y which represents a room that will be built in the future
	private void addNextRoom(int x, int y) {

		// Ensure boundary checking
		if (x >= 0 && x < MAXROOMS && y >= 0 && y < MAXROOMS) {

			// And we really only want to build so far ahead
			if (newRoomList.size() < 200) {
				
				newRoomList.add(new Coordinate(x, y));
			}
			
			// If we get too far ahead of ourselves, we stop for a bit
			else if (newRoomList.size() > 199) {
				
				stopAddingRooms = true;
			}
		}
	}
	
	// toString method
	public String toString() {
		
		return worldName + "!\n\r" + MAXROOMS + "\n\r";
	}
	
	// equals method
	public boolean equals(World oneWorld) {
		
		if (toString().equals(oneWorld.toString())) {
			
			return true;
		}
		
		return false;
	}
	
} // End of World Class
