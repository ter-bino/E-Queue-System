package shared_classes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

/**
 * This abstract class will be inherited by Drink, Beverage and Dessert
 * This represents every item for sale in the Canteen as Items object
 * Could make one without the subclasses Drink, Beverage and Dessert but
 * 	they are in case I plan to add variations in the future (sizes, flavors, etc.)
 */
public abstract class Item implements Serializable{
	
	//file for tracking the last item ID used
	final private static File ID_FILE = new File("item.dat");

	private static final long serialVersionUID = 8561921075401602583L;
	private final String itemID;
	private String category;
	private String name;
	private double price;
	
	/***
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
	
	//getters
	public String getItemID() {
		return this.itemID;
	}
	public String getCategory() {
		return this.category;
	}
	public String getName() {
		return this.name;
	}
	public double getPrice() {
		return this.price;
	}
	//setters
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
	public void setName(String name) {
		this.name = name;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	//Returns info about the item in a sentence form
	public String toString() {
		return this.name +  " - a " + this.category.toLowerCase() + " that costs P" + this.price;
	}
	
	/**
	 * static method for getting a String to set as ID of an Items object
	 * NOTE: LIMIT THE ARGUMENTS PASSED TO VALUES "meal", "beverage" AND "dessert"
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
