/**
 *<pre>
 * Purpose
 *		
 *		This class holds the collection of item characteristics. Think of it as the bulk of what
 *		an item actually is, as opposed to the Item class which holds location information and
 *		an object ID.
 *		
 * Structure / Process
 *		
 *		ItemDescriptions are a field in the Item object which holds the Item's tangible
 *		characteristics. Every item has an ItemDescription. It is saved and loaded via
 *		flat files, as well as automatically generated when the code calls for it.
 *</pre>	
 * @author Nicholas Warner
 * @version 5.1, May 2015
 */
public class ItemDescription {

	// These represent the locations where the Items are worn
	/** A constant indicating the Held Location in the Worn Location Array */
	public static final int LOCATION_HELD		= 0;
	/** A constant indicating the Head Location in the Worn Location Array */
	public static final int LOCATION_HEAD		= 1;
	/** A constant indicating the Chest Location in the Worn Location Array */
	public static final int LOCATION_CHEST		= 2;
	/** A constant indicating the Arms Location in the Worn Location Array */
	public static final int LOCATION_ARMS		= 3;
	/** A constant indicating the Hands Location in the Worn Location Array */
	public static final int LOCATION_HANDS		= 4;
	/** A constant indicating the Waist Location in the Worn Location Array */
	public static final int LOCATION_WAIST		= 5;
	/** A constant indicating the Legs Location in the Worn Location Array */
	public static final int LOCATION_LEGS		= 6;
	/** A constant indicating the Boots Location in the Worn Location Array */
	public static final int LOCATION_BOOTS		= 7;
	/** A constant indicating the Wield Location in the Worn Location Array */
	public static final int LOCATION_WIELD		= 8;
	
	/** A constant to represent the number of worn locations */
	public static final int NUMBER_WORN_ON_LOCATIONS = 9;
	
	/** A list which helps randomize the Head Item's name */
	public static final String[] WORN_ON_HEAD	=			{	"Helmet",
																"Circlet",
																"Halo",
																"Helm",
																"Skull Cap",
																"Hood" };

	/** A list which helps randomize the Body Item's name */																
	public static final String[] WORN_ON_BODY	=			{	"Breast Plate",
																"Chest Plate",
																"Chain Mail",
																"Scale Mail",
																"Robes",
																"Tunic",
																"Vest" };

	/** A list which helps randomize the Arms Item's name */
	public static final String[] WORN_ON_ARMS	=			{	"Sleeves",
																"Vambraces",
																"Arm Bands",
																"Arm Plates",
																"Arm Guards" };

	/** A list which helps randomize the Hands Item's name */
	public static final String[] WORN_ON_HANDS	=			{	"Gloves",
																"Gauntlets",
																"Mittens", };

	/** A list which helps randomize the Waist Item's name */
	public static final String[] WORN_ON_WAIST	=			{	"Belt",
																"Cord",
																"Sash",
																"Girth",
																"Ribbon" };

	/** A list which helps randomize the Legs Item's name */					
	public static final String[] WORN_ON_LEGS	=			{	"Legplates",
																"Shinguards",
																"Greaves",
																"Pants",
																"Leggings" };

	/** A list which helps randomize the Feet Item's name */
	public static final String[] WORN_ON_FEET	=			{	"Boots",
																"Sandals",
																"Shoes",
																"Chain Boots",
																"Plate Boots" };

	/** A list which helps randomize the Item's Metal name */
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

	/** 
	 * This is specifically used for changing the color of the ASCII Warrior. Each Metal type
	 *	has a corresponding color which is then represented in the ASCII Warrior's equipment.
	 */
	private static final String[] itemMetalTypesColor =		{ "#y", "#W", "#Y", "#w", "#w", "#W",
															  "#W", "#G", "#w", "#P", "#C", "#C",
															  "#R", "#g", "#G", "#w", "#y", "#W",
															  "#C", "#y" };
	/** A list of metal names without their colors added. */
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
	
	/** 
	 * This list corresponds to what each metal type offers in terms of statistic bonuses. For
	 * example, steel offers a bonus to Strength while granite offers a bonus to mana. 
	 */
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


	/** A list of modifier names, helps with randomization. */
	private static final String[] itemModifierTypes =		{	"#Ya God#n",
																"#CHeaven#n",
																"#Ythe Blessed#n",
																"#Rthe King#n",
																"#Pa Seer#n",
																"#Gthe Lord#n",
																"#Wa Champion#n",
																"#ya Master#n" };
	
	/** A list of modifier names without their corresponding color codes. */
	private static final String[] itemModifierTypeNames =	{	"a god",
																"heaven",
																"the blessed",
																"the king",
																"a seer",
																"the lord",
																"a champion",
																"a master" };

	/**
	 * The actual percent modifier for each of the modifier names. i.e. Godly is 1.9 or 190%,
	 * the king is 1.6 or 160%, etc. These represent the actual bonus multiple to the stat of 
	 * the Item; for example, a Steel Helmet of a God will provide a 190% bonus to Strength 
	 * (or some variation of this).
	 */
	private static final double[] itemModifierTypeValues =	{	1.9,	// godly
																1.8,	// heavenly
																1.7,	// blessed
																1.6,	// king's
																1.5,	// seer's
																1.4,	// lord's
																1.3,	// champion's
																1.2, };	// master's

	/** The prefix of the item (metal type). */
	private String prefix;
	/** The suffix of the item (modifier name). */
	private String suffix;
	/** The actual name of the item. */
	private String itemName;
	/** The type of item; helmet, boots, gloves, etc. */
	private String itemSimpleName;
	/** The actual behind the scenes nuts and bolts. */
	private String itemMetalTypeName;
	/** The value of the item metal type. */
	private int itemMetalTypeValue;
	/** The modifier's name */
	private double modifierName;
	/** The modifier's value */
	private double modifierValue;

	/** The iLevel of the item as a representation of how strong the item is. */
	private int iLevel;
	/** The location where the item is worn. */
	private int locationWorn;
	
	/** Default constructor, setting all Strings to "" and ints/doubles to 0. */
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

	/** 
	 * Paramterized constructor; used when loading a profile. 
	 *
	 * @param prefix The prefix of the item, e.g. the metal type.
	 * @param suffix The modifier of the item, e.g. "the lord", "a champion", etc.
	 * @param itemName The Item's referenced name. No color, etc.
	 * @param itemSimpleName The basic randomized name from the list. "Halo", "Shoes", etc.
	 * @param itemMetalTypeName The name of the metal type of the Item.
	 * @param itemMetalTypeValue The value of the metal type of the Item.
	 * @param modifierName The name of the modifier applied to the item.
	 * @param modifierValue The value for the modifier.
	 * @param iLevel The Item's Level, as an indicator of the power of the item.
	 * @param locationWorn A reference to where the item is worn on the body.
	 */
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
	
	/** 
	 * Paramterized constructor; used when dynamically creating Items.
	 *
	 * @param locationWorn A reference to where the item is worn on the body.
	 * @param iLevel The Item's Level, as an indicator of the power of the Item.
	 */
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

	/** 
	 * A method to get the Item's simple name.
	 *
	 * @return Returns the item's simple name.
	 */
    public String getSimpleName() {
    	
    	return itemSimpleName;
    }

	/** 
	 * A method to get the Item's worn on location.
	 *
	 * @return Returns the item's locationWorn value.
	 */
    public int getLocationWorn() {
    	
    	return locationWorn;
    }

	/**
	 * A method to get the iLevel of the Item.
	 *
	 * @return Returns the Item's iLevel.
	 */
    public int getILevel() {
    	
    	return iLevel;
    }

	/** 
	 * A method to get the name of the Item.
	 *
	 * @return Returns the Item's name.
	 */
    public String getName() {
    	
    	return itemName;
    }

	/**
	 * A method to set the name of the Item.
	 *
	 * @param name The new name of the Item.
	 */
    public void setName(String name) {
    	
    	itemName = name;
    }

	/**
	 * A method to set the iLevel of the Item.
	 *
	 * @param iLevel The new iLevel of the Item.
	 */
    public void setILevel(int iLevel) {
    	
    	this.iLevel = iLevel;
    }

	/** 
	 * A method to return the Item's fields, identifying the item.
	 *
	 * @return A String with an output of all of the Item's fields.
	 */
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
    
    /**
	 * A method to test whether two ItemDescriptions are equivalent.
	 *
	 * @param iDesc The ItemDescription we are comparing the Item against.
	 * @return Returns whether the two ItemDescriptions are equal(true) or not(false).
	 */
    public boolean equals(ItemDescription iDesc) {
    	
    	if (toString().equals(iDesc.toString())) {
    		
    		return true;
    	}
    	
    	return false;
    }
}
