// Import our packages
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileOutputStream;

/**
 *<pre>
 *	Purpose
 *
 *		The World is a class that constitutes the overall physcial JavaWorld game. It contains a
 *		list of all rooms, and is part of the initial startup for loading rooms and generating
 *		dynamic rooms.
 *
 *	Structure / Process
 *
 *		In the GameServer class, the World class is used to initially load the HOMELOCATION and it's
 *		surrounding rooms from a flat file. Then the World class is called to dynamically generate
 *		the surrounding world using the pseudo-random Seed class.
 *</pre>
 * @author Nicholas Warner
 * @version 5.1, June 2015
 */
public class World {
	
	/** The starting zone flat file. */
    public static final String ZONEFILE		= "../zones/StartingZone.zone";
	/** The maximum number of x rooms and y rooms (MAXROOMS^2 for max number of rooms). */
    public static final int MAXROOMS		= 200;
    /** The home location of (100, 100), safety! */
	public static final int HOMELOCATION   	= 100;

	/** The shorthand for the North direction. */
	private final int NORTH	= 0;
	/** The shorthand for the East direction. */
	private final int EAST	= 1;
	/** The shorthand for the South direction. */
	private final int SOUTH	= 2;
	/** The shorthand for the West direction. */
	private final int WEST	= 3;

	/** A constant representing the FOREST area type. */
	private final int FOREST	= 0;
	/** A constant representing the LAKE area type. */
	private final int LAKE		= 1;
	/** A constant representing the CITY area type. */
	private final int CITY		= 2;
	/** A constant representing the PARK area type. */
	private final int PARK		= 3;
	/** A constant representing the CAVE area type. */
	private final int CAVE		= 4;
	/** A constant representing the MOUNTAIN area type. */
	private final int MOUNTAIN	= 5;

	/** A String array of the different area types. */
	private final String[] roomTypes	= { "Forest", "Lake", "City", "Park", "Cave", "Mountain" };

	/** 
	 * This holds the number of queued rooms to be built. When dynamically building the world,
	 * each room is built and there is a random chance that an adjacent room will be built. If
	 * the adjacent room is to be built, it is added to the queue of rooms to be built. If this
	 * queue grows to MAX_QUEUED_ROOMS, the queue cannot grow any larger.
	 */
	private final int MAX_QUEUED_ROOMS	= 200;

	/** This is the actual "world"; a 2D array of Rooms. */
	private static Room[][] rooms = new Room[MAXROOMS][MAXROOMS];

	/** The World is the world is the world. This is the World. */
	private static World world = null;
	/** The name of the world; JavaWorld! */
	private String worldName;
	/** An arrayList for rooms which are going to be dynamically generated soon. */
	private ArrayList<Coordinate> newRoomList;
	/** Part of the newRoomList. */
	private int buildingNextRoom;
	/** Knowing where we've already built. */
	private int builtNextRoom;
	/** 
	 * A boolean which controls whether we cannot building, or if we've reached the
	 * MAX_QUEUED_ROOMS, then to stop building.
	 */
	private boolean stopAddingRooms;

	/** The default World constructor. */
	public World() {
		
		worldName			= "";
		world				= this;
		newRoomList			= new ArrayList<Coordinate>();
		
		buildingNextRoom	= 0;
		builtNextRoom		= 0;
		stopAddingRooms		= false;
	}
	
	/**
	 * A constructor which takes an argument for the name of the World.
	 *
	 * @param name The name of the World to be constructed.
	 */
	public World(String name) {
		
		worldName			= name;
		world				= this;
		newRoomList			= new ArrayList<Coordinate>();
		
		buildingNextRoom	= 0;
		builtNextRoom		= 0;
		stopAddingRooms		= false;
	}

	/** 
	 * A method to get a reference to the current World object.
	 *
	 * @return Returns a reference to the currently instantiated World object.
	 */
	public static World getWorld() {
		
		return world;
	}
	
	/**
	 * A method to return a reference to a Room given by an (x, y) coordinate.
	 *
	 * @param x The X coordinate of the Room.
	 * @param y The Y coordinate of the Room.
	 * @return Returns a reference to the Room.
	 */
	public static Room getRoom(int x, int y) {
		
		// Make sure with bounds checking
		if (x < MAXROOMS && y < MAXROOMS && x >= 0 && y >= 0) {

			if (rooms[x][y] != null) {

				return rooms[x][y];
			}
		}
		
		return null;
	}

	/**
	 * A method to get the name of the World.
	 *
	 * @return Returns a String containing the name of the World.
	 */
	public String getWorldName() {
		
		return worldName;
	}

	/**
	 * A method to return the coordiantes of the next room we'll dynamically build.
	 *
	 * @return Returns the coordinates of the next room to be dynamically built.
	 */
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

	/**
	 * A method to add a given room to the specified (x, y).
	 *
	 * @param newRoom The new Room to be added to the array of Rooms.
	 * @param x The X coordinate of the Room to be added.
	 * @param y The Y coordinate of the Room to be added.
	 */
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

	/**
	 * A method to test whether a room exists or not.
	 *
	 * @param x The X coordinate of the Room.
	 * @param y The Y coordinate of the Room.
	 * @return Returns true if the room exists and false if not.
	 */
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

	/** A method to load up the predefined, flat file, home zone. */
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

	/**
	 * A method to dynamically build the world.
	 *
	 * @param sizeOfWorld The size of the world we want to build.
	 */
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
	
	/** A method to build a room given two coordinates and a room type.
	 *
	 * @param locationX The X coordinate of the Room.
	 * @param locationY The Y coordinate of the Room.
	 * @param rType The Area type of the Room.
	 * @return Returns a reference to a new Room object with the given x, y, and rType.
	 */
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
	
	/**
	 * A method to check the exits to see which directions have exits and which don't.
	 *
	 * @param roomExists An array which indicates whether a room exists in any of the given
	 *						directions of NORTH, SOUTH, EAST, WEST.
	 * @param roomX The X coordinate of the Room.
	 * @param roomY The Y coordinate of the Room.
	 */
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
	
	/**
	 * A method to add a coordinate object for x, y which represents a room that will 
	 * be built in the future. This is used in the dynamic room building code and this
	 * is an example of a queued room to be built.
	 *
	 * @param x The X coordinate of the Room to be built.
	 * @param y The Y coordinate of the Room to be built.
	 */
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
	
	/**
	 * A method which returns some unique information about this World object.
	 *
	 * @return Returns a String containing the name of the World and the number of
	 *			Rooms the world could have at a maximum.
	 */
	public String toString() {
		
		return worldName + "!\n\r" + MAXROOMS + "\n\r";
	}
	
	/**
	 * A method which tests whether a given World Object is equal to this World.
	 *
	 * @param oneWorld The given World object to be tested against for equality.
	 * @return Returns true if the World objects are equal and false if the
	 *			World objects are not equal.
	 */
	public boolean equals(World oneWorld) {
		
		if (toString().equals(oneWorld.toString())) {
			
			return true;
		}
		
		return false;
	}
	
} // End of World Class
