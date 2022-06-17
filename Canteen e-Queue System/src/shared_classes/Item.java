package shared_classes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

/**
 * This abstract class will be inherited by Meal, Beverage and Dessert
 * This represents every item for sale in the Canteen as Items object
 * Could make one without the subclasses Drink, Beverage and Dessert but
 * 	they are in case I plan to add variations in the future (sizes, flavors, etc.)
 * @author alter
 */
public abstract class Item implements Serializable{
	
	//file for tracking the last item ID used
	final private static File ID_FILE = new File("item.dat");

	private static final long serialVersionUID = 8561921075401602583L;
	private final String itemID;
	private String category;
	private String name;
	private double price;
	
	/**
	 * Constructor
	 * @param name		- name of the item
	 * @param price		- price of the item
	 * @param itemID	- item id, automatically set from subclasses of Item
	 */
	public Item(String name, double price, String itemID) {
		this.name = name;
		this.price = price;
		this.itemID = itemID;
	}
	
	/**
	 * @return - id of this item
	 */
	public String getItemID() {
		return this.itemID;
	}
	/**
	 * @return - category of this item
	 */
	public String getCategory() {
		return this.category;
	}
	/**
	 * @return - name of this item
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * @return - price of this item
	 */
	public double getPrice() {
		return this.price;
	}
	/**
	 * Setter for the category of the item. Valid arguments are
	 * "Meal", "Beverage", "Dessert". If the argument is none
	 * of these 3, it will be set to a Meal.
	 * @param category - category for this item
	 */
	public void setCategory(String category) {
		//limit categories to "meal", "beverage" and "dessert". Default is "meal"
		switch(category.toLowerCase()) {
		case "meal":
			this.category = "Meal";
			break;
		case "beverage":
			this.category = "Beverage";
			break;
		case "dessert":
			this.category = "Dessert";
			break;
		default:
			this.category = "Meal";
		}
	}
	/**
	 * @param name - name for this item
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param price - price for this item
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	
	/**
	 * Returns info about the item in a sentence form.
	 */
	public String toString() {
		return this.name +  " - a " + this.category.toLowerCase() + " that costs P" + this.price;
	}
	
	/**
	 * This MUST called when instantiating a subclass of an Item.
	 * Fetches the last Item ID used based on category to define
	 * the item id to assign to the newly instantiated one.
	 * NOTE: LIMIT THE ARGUMENTS PASSED TO VALUES "meal", "beverage" AND "dessert"
	 * @param category - category of the item being instantiated
	 * @return - itemID to assign to the newly instantiated item
	 * @throws IOException
	 */
	protected static String setID(String category) throws IOException {
		
		//for reading and modifying ID_FILE;
		Scanner reader;
		FileWriter writer;;
		
		//tracks the no. of items added (m - meal, b - beverage, d - drink)
		int m, b, d;
		
		//string to return
		String result = null;
		
		//If ID_FILE is not yet available, create it
		if(!ID_FILE.exists()) {
			
			ID_FILE.createNewFile();
			//start the tracker no. from Zeroes
			m = 0; b = 0; d = 0;
			
		//If ID_FILE is already available;	
		} else {
			
			reader = new Scanner(ID_FILE);
			//get tracker no. from ID_FILE
			m = reader.nextInt();
			b = reader.nextInt();
			d = reader.nextInt();
			//close reader
			reader.close();

		}
		
		//Identify ID to return
		switch(category.toLowerCase()) {
		case ("meal"):
			m++;
			result = "M" + m;
			break;
		case ("beverage"):
			b++;
			result = "B" + b;
			break;
		case ("dessert"):
			d++;
			result = "D" + d;
			break;
		}
		
		//update content of ID_FILE
		writer = new FileWriter(ID_FILE);
		writer.write(m + " " + b + " " + d);
		
		//close writer
		writer.close();
		
		//return the result
		return result;
	}
}
