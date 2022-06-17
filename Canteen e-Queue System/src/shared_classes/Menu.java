package shared_classes;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import equeue_server.ServerFunctions;


/**
 * Class to represent the Menu across the
 * interactive system
 * @author alter
 */
public class Menu implements Serializable{
	
	private static final long serialVersionUID = 3854637659904755479L;
	
	/**
	 * Magic constant for Meal
	 */
	public final static byte MEAL = 0;
	/**
	 * Magic constant for Beverage
	 */
	public final static byte BEVERAGE = 1;
	/**
	 * Magic constant for Dessert
	 */
	public final static byte DESSERT = 2;

	//the actual collection of the items
	private HashMap<String, Item> menu = new HashMap<String, Item>();
	
	/**
	 * Instantiates and adds an Item's subclass to the Menu.
	 * Category is based on 3rd argument.
	 *  - 0 for meal
	 *  - 1 for beverage
	 *  - 2 for dessert
	 * @param name - name for the item
	 * @param price - price for the item
	 * @param type - type/category of the item
	 * @throws IOException
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
	
	/**
	 * Removes an item from the menu.
	 * NOTE: make sure input String is a verified, existing
	 *  itemID from the menu before calling this method
	 * @param itemID - the id of the item to remove in this menu
	 * @throws IOException
	 */
	public void remove(String itemID) throws IOException{
		
		menu.remove(itemID.toUpperCase());
		ServerFunctions.updateMenu();
		
	}
	
	/**
	 * Edit an item from the Menu
	 * NOTE: itemID must be validated as existing item in
	 * the menu before calling this method!
	 * @param itemID - id of item to edit
	 * @param newName - new name of the item
	 * @param newPrice - new price of the item
	 * @throws IOException
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
	
	/**
	 * this getter returns the Menu in the form of a HashMap<String,Item>
	 * where the String is the Item ID and Item is the Item object
	 * itself. The purpose of this to give clients a menu without
	 * the menu object's method. For read-only purpose.
	 * @return - the menu as a HashMap
	 */
	public HashMap<String, Item> getMenu(){
		return this.menu;
	}
	
	/**
	 * returns the values of the HashMap menu as a collection
	 * (for easier accesing)
	 * @return - values of the HashMap menu
	 */
	public Collection<Item> values() {
		return menu.values();
	}
}
