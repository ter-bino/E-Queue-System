package shared_classes;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import equeue_server.ServerFunctions;


/**
 * class to represent the Menu across the
 * interactive system
 */
public class Menu implements Serializable{
	
	private static final long serialVersionUID = 3854637659904755479L;
	
	//magic constants for Item 
	public final static byte MEAL = 0;
	public final static byte BEVERAGE = 1;
	public final static byte DESSERT = 2;

	//the actual collection of the items
	private HashMap<String, Item> menu = new HashMap<String, Item>();
	
	/**
	 * adds an Item subclass to the Menu, according to 3rd argument
	 *  - 0 for meal
	 *  - 1 for beverage
	 *  - 2 for dessert
	 *  if argument is neither of the 3, then item added is a Meal by deafult.
	 */
	public void add(String name, double price, byte type) throws IOException{
		
		Item item;
		
		switch(type) {
		case MEAL:
			item = new Meal(name, price);
			menu.put(item.getItemID(), item);
			break;
		case BEVERAGE:
			item = new Beverage(name, price);
			menu.put(item.getItemID(), item);
			break;
		case DESSERT:
			item = new Dessert(name, price);
			menu.put(item.getItemID(), item);
			break;
		default:
			item = new Meal(name, price);
			menu.put(item.getItemID(), item);
		}
		ServerFunctions.updateMenu();
	}
	
	/**
	 * Remove an item from menu
	 * NOTE: make sure input String is a verified, existing
	 *  itemID from the menu before calling this method
	 */
	public void remove(String itemID) throws IOException{
		
		menu.remove(itemID.toUpperCase());
		ServerFunctions.updateMenu();
		
	}
	
	/**
	 * Edit an item from the Menu
	 * NOTE: itemID must be validated as existing item in
	 * the menu before calling this method!
	 */
	public void edit(String itemID, String newName, double newPrice) throws IOException {
		
		menu.get(itemID).setName(newName);
		menu.get(itemID).setPrice(newPrice);
		ServerFunctions.updateMenu();
	}
	
	/**
	 * this getter returns the Menu in the form of a HashMap<String,Item>
	 * where the String is the Item ID and Item is the Item object
	 * itself
	 */
	public HashMap<String, Item> getMenu(){
		return this.menu;
	}

	/**
	 * returns the values of the HashMap menu
	 * (for easier accesing)
	 */
	public Collection<Item> values() {
		return menu.values();
	}
}
