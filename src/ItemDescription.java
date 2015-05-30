/* Purpose
 *		
 *		This class holds the collection of item characteristics. Think of it as the bulk of what
 *		an item actually is, as opposed to the Item class which holds location information and
 *		an object ID.
 *		
 * Algorithm
 *		
 *		1. Declare the ItemDescription class
 *		2. Declare some constants to represent locations where items can be worn
 *		3. Declare a constant to indicate the number of worn locations
 *		4. Declare String and int arrays which give the items some random names and modifiers
 *		5. Declare a String for the Item's randomized prefix
 *		6. Declare a String for the Item's randomized suffix
 *		7. Declare a String for the item's fully constituted name
 *		8. Declare a String for the item's simple name (shield, helmet, boots)
 *		9. Declare a String for the metal type's name (gold, pyrite, vanadium)
 *		10. Declare an int to hold the modifier type for the item
 *		11. Declare a double to hold the modifier's name
 *		11. Declare a double to hold the modifier's value
 *		12. Declare an int to represent the level of the item (how good it is)
 *		13. Declare an int to represent where the item is worn on the body
 *		14. Declare a default constructor
 *		15. Declare two paramterized constructors; one for randomized items and one
 *			for non-randomized items (i.e. when loading a player profile and its inventory)
 *		16. Declare Accessor Methods
 *		17. Declare Mutator Methods
 *		18. Declare a toString method and an equals method
 *		
 * Structure / Process
 *		
 *		ItemDescriptions are a field in the Item object which holds the Item's tangible
 *		characteristics. Every item has an ItemDescription. It is saved and loaded via
 *		flat files, as well as automatically generated when the code calls for it.
 *		
 *	Author			- Nicholas Warner
 *	Created			- 4/24/2015
 *	Last Updated	- 5/1/2015
 */

public class ItemDescription {

	// These represent the locations where the Items are worn
	public static final int LOCATION_HELD		= 0;
	public static final int LOCATION_HEAD		= 1;
	public static final int LOCATION_CHEST		= 2;
	public static final int LOCATION_ARMS		= 3;
	public static final int LOCATION_HANDS		= 4;
	public static final int LOCATION_WAIST		= 5;
	public static final int LOCATION_LEGS		= 6;
	public static final int LOCATION_BOOTS		= 7;
	public static final int LOCATION_WIELD		= 8;
	
	// This represents the number of worn locations
	public static final int NUMBER_WORN_ON_LOCATIONS = 9;
	
	// These are the lists which help randomize the Item's characteristics
	public static final String[] WORN_ON_HEAD	=			{	"Helmet",
																"Circlet",
																"Halo",
																"Helm",
																"Skull Cap",
																"Hood" };
																
	public static final String[] WORN_ON_BODY	=			{	"Breast Plate",
																"Chest Plate",
																"Chain Mail",
																"Scale Mail",
																"Robes",
																"Tunic",
																"Vest" };

	public static final String[] WORN_ON_ARMS	=			{	"Sleeves",
																"Vambraces",
																"Arm Bands",
																"Arm Plates",
																"Arm Guards" };

	public static final String[] WORN_ON_HANDS	=			{	"Gloves",
																"Gauntlets",
																"Mittens", };
	
	public static final String[] WORN_ON_WAIST	=			{	"Belt",
																"Cord",
																"Sash",
																"Girth",
																"Ribbon" };
																
	public static final String[] WORN_ON_LEGS	=			{	"Legplates",
																"Shinguards",
																"Greaves",
																"Pants",
																"Leggings" };

	public static final String[] WORN_ON_FEET	=			{	"Boots",
																"Sandals",
																"Shoes",
																"Chain Boots",
																"Plate Boots" };

	private static final String[] itemMetalTypes =			{	"#yPyrite#n",
																"#WSteel#n",
																"#YGold#n",
																"#wGranite#n",
																"#wOnyx#n",
																"#WOpal#n",
																"#WSilver#n",
																"#GEmerald#n",
																"#wPlatinum#n",
																"#PTitanium#n",
																"#CCrystal#n",
																"#CDiamond#n",
																"#RVanadium#n",
																"#gMalachite#n",
																"#GSnakeskin#n",
																"#wWolven Pelt#n",
																"#yLeather#n",
																"#WSheepskin#n",
																"#CSilk#n",
																"#yBronze#n" };

	// This is specifically used for changing the color of the ASCII Warrior
	private static final String[] itemMetalTypesColor =		{ "#y", "#W", "#Y", "#w", "#w", "#W",
															  "#W", "#G", "#w", "#P", "#C", "#C",
															  "#R", "#g", "#G", "#w", "#y", "#W",
															  "#C", "#y" };

	private static final String[] itemMetalTypeNames = 		{	"pyrite",
																"steel",
																"gold",
																"granite",
																"onyx",
																"opal",
																"silver",
																"emerald",
																"platinum",
																"titanium",
																"crystal",
																"diamond",
																"vanadium",
																"malachite",
																"snakeskin",
																"wolven pelt",
																"leather",
																"sheepskin",
																"silk",
																"bronze" };
	
	// These correspond to what each metal type offers in terms of statistic bonuses
	private static final int[] itemMetalTypeValues =		{	Player.PLAYER_HEALTH,
																Player.PLAYER_STRENGTH,
																Player.PLAYER_HEALTH,
																Player.PLAYER_MANA,
																Player.PLAYER_DEXTERITY,
																Player.PLAYER_INTELLIGENCE,
																Player.PLAYER_FORTITUDE,
																Player.PLAYER_WISDOM,
																Player.PLAYER_STRENGTH,
																Player.PLAYER_FORTITUDE,
																Player.PLAYER_MANA,
																Player.PLAYER_INTELLIGENCE,
																Player.PLAYER_WISDOM,
																Player.PLAYER_DEXTERITY,
																Player.PLAYER_DEXTERITY,
																Player.PLAYER_HEALTH,
																Player.PLAYER_NONE,
																Player.PLAYER_INTELLIGENCE,
																Player.PLAYER_FORTITUDE 	};


	private static final String[] itemModifierTypes =		{	"#Ya God#n",
																"#CHeaven#n",
																"#Ythe Blessed#n",
																"#Rthe King#n",
																"#Pa Seer#n",
																"#Gthe Lord#n",
																"#Wa Champion#n",
																"#ya Master#n" };
	
	private static final String[] itemModifierTypeNames =	{	"a god",
																"heaven",
																"the blessed",
																"the king",
																"a seer",
																"the lord",
																"a champion",
																"a master" };

	// These represent the actual bonus multiple to the stat of the Item; for example, a Steel
	// Helmet of a God will provide a 190% bonus to Strength (or some variation of this)
	private static final double[] itemModifierTypeValues =	{	1.9,	// godly
																1.8,	// heavenly
																1.7,	// blessed
																1.6,	// king's
																1.5,	// seer's
																1.4,	// lord's
																1.3,	// champion's
																1.2, };	// master's

	// The prefix of the item (metal type)
	private String prefix;
	// The suffix of the item (modifier name)
	private String suffix;
	// The actual name of the item
	private String itemName;
	// The type of item; helmet, boots, gloves, etc
	private String itemSimpleName;
	// The actual behind the scenes nuts and bolts
	private String itemMetalTypeName;
	private int itemMetalTypeValue;
	private double modifierName;
	private double modifierValue;

	// The iLevel of the item as a representation of how strong the item is
	private int iLevel;
	// The location where the item is worn
	private int locationWorn;
	
	// Default constructor
	public ItemDescription() {
		
		prefix				= "";
		suffix				= "";
		itemName			= "";
		itemSimpleName		= "";
		itemMetalTypeName	= "";
		itemMetalTypeValue	= 0;
		modifierName		= 0;
		modifierValue		= 0;
		iLevel				= 0;
		locationWorn		= 0;
	}

	// Paramterized constructor; used when loading a profile
	public ItemDescription(String prefix, String suffix, String itemName, String itemSimpleName,
						   String itemMetalTypeName, int itemMetalTypeValue, double modifierName,
						   double modifierValue, int iLevel, int locationWorn) {
		
		this.prefix 			= prefix;
		this.suffix 			= suffix;
		this.itemName 			= itemName;
		this.itemSimpleName 	= itemSimpleName;
		this.itemMetalTypeName	= itemMetalTypeName;
		this.itemMetalTypeValue	= itemMetalTypeValue;
		this.modifierName		= modifierName;
		this.modifierValue		= modifierValue;
		this.iLevel				= iLevel;
		this.locationWorn		= locationWorn;
	}
	
	// Paramterized constructor; used when dynamically creating Items
    public ItemDescription(int locationWorn, int iLevel) {

		// Get a random metal
		int randomSelection = ToolKit.rand(0, itemMetalTypes.length);

		// Make sure the iLevel is appropriate
		this.iLevel = (iLevel > 70 ? 70 : iLevel);		
		if (iLevel <= 0) {
			
			iLevel = 1;
		}

		// Set the wear location
		this.locationWorn = locationWorn;
		
		// Figure out what the item actually is; gloves, arm plates, helmet, ...
		switch(locationWorn) {
			
			// Choose a random name from its given type
			case 1: itemSimpleName = WORN_ON_HEAD[ToolKit.rand(0, WORN_ON_HEAD.length)]; break;
			case 2: itemSimpleName = WORN_ON_BODY[ToolKit.rand(0, WORN_ON_BODY.length)]; break;
			case 3: itemSimpleName = WORN_ON_ARMS[ToolKit.rand(0, WORN_ON_ARMS.length)]; break;
			case 4: itemSimpleName = WORN_ON_HANDS[ToolKit.rand(0, WORN_ON_HANDS.length)]; break;
			case 5: itemSimpleName = WORN_ON_WAIST[ToolKit.rand(0, WORN_ON_WAIST.length)]; break;
			case 6: itemSimpleName = WORN_ON_LEGS[ToolKit.rand(0, WORN_ON_LEGS.length)]; break;
			case 7: itemSimpleName = WORN_ON_FEET[ToolKit.rand(0, WORN_ON_FEET.length)]; break;
			// Used for held items where locationWorn == 0
			default: itemSimpleName = "Object"; break;
		}

		// Set the Prefix
		prefix				= itemMetalTypes[randomSelection];
		// Set the metal type name
		itemMetalTypeName	= itemMetalTypeNames[randomSelection];
		// Set the suffix
		suffix				= itemModifierTypes[7 - (iLevel / 10)];
		
		// Build the item!
		itemName = prefix + " " + itemSimpleName + " of " + suffix + "#n";
    }

	// Accessor Methods    
    public String getSimpleName() {
    	
    	return itemSimpleName;
    }
        
    public int getLocationWorn() {
    	
    	return locationWorn;
    }
    
    public int getILevel() {
    	
    	return iLevel;
    }
    
    public String getName() {
    	
    	return itemName;
    }
    
    // Mutator Methods
    public void setName(String name) {
    	
    	itemName = name;
    }
    
    public void setILevel(int iLevel) {
    	
    	this.iLevel = iLevel;
    }

	// toString method    
    public String toString() {
    	
    	return	prefix + "\n" +
    			suffix + "\n" +
    			itemName + "\n" +
    			itemSimpleName + "\n" +
    			itemMetalTypeName + "\n" +
    			itemMetalTypeValue + "\n" +
    			modifierName + "\n" +
    			modifierValue + "\n" +
    			iLevel + "\n" +
    			locationWorn + "\n";
    }
    
    // equals method
    public boolean equals(ItemDescription iDesc) {
    	
    	if (toString().equals(iDesc.toString())) {
    		
    		return true;
    	}
    	
    	return false;
    }
}