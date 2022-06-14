package equeue_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import shared_classes.Item;
import shared_classes.Menu;
import shared_classes.Order;

/**
 * This class contains multiple STATIC methods that
 * are called when clicking on different buttons in
 * the GUI of the Server application.
 */
public class ServerFunctions {

	/**
	 * Magic constants as valid arguments for refreshOrder(int filter)
	 */
	static final int STATUS_WAITING = 0;
	static final int STATUS_PROCESSING = 1;
	static final int STATUS_SERVING = 2;
	static final int STATUS_COMPLETED = 3;
	
	/**
	 * Loads the MENU_FILE for the program if it exists
	 *  - otherwise create it.
	 */
	public static void loadMenu() {
		
		File file = Server.MENU_FILE;
		
		//if file does not exist
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				//exception already prevented by the if above
			}
			
			Server.menu = new Menu();
			try {
				updateMenu();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.toString());
			}
		
			
		//if file exists
		} else {
			try {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
			
				Server.menu = (Menu) ois.readObject();
				
				ois.close();
				
			} catch (IOException | ClassNotFoundException e){
				JOptionPane.showMessageDialog(null, e.toString());
				
			}
		}
	}
	
	/**
	 * Updates the menu_file
	 * MUST BE CALLED everytime a new item is
	 * added to or removed from menu
	 */
	public static void updateMenu() throws IOException {
		
		FileOutputStream fos = new FileOutputStream(Server.MENU_FILE);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		oos.writeObject(Server.menu);
		
		oos.close();
		
	}
	
	/**
	 * Adds an item to the Menu by calling GlobalComponent.menu.add();
	 * Opens a series of JOptionPane asking for:
	 *  - Item name
	 *  - Item price
	 *  - Item category
	 */
	public static void addItem(JFrame frame) {
		
		//Ask for item name, return right away if cancelled
		String name = JOptionPane.showInputDialog(frame, "Enter item name: ", "Adding New Item", JOptionPane.QUESTION_MESSAGE);
		if(name==null || name.isBlank()) {
			JOptionPane.showMessageDialog(frame,  "Adding item cancelled.", "Cancelled", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		//Ask for price (repeats for invalid price), return right away if cancelled
		double price;
		while(true) {
			String sPrice = JOptionPane.showInputDialog(frame, "Enter item price: ", "Adding New Item", JOptionPane.QUESTION_MESSAGE);
			if(sPrice==null){
				JOptionPane.showMessageDialog(frame,  "Adding item cancelled.", "Cancelled", JOptionPane.WARNING_MESSAGE);
				return;
			}
			else {
				try {
					price = Double.parseDouble(sPrice);
					break;
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(frame, "Invalid price!", "Invalid", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		
		//Ask for item category, return right away if cancelled
		String[] options = new String[] {"Meal", "Beverage", "Dessert", "Cancel"}; //choices
		int type = JOptionPane.showOptionDialog(frame,
												"Select item category:",
												"Adding New Item",
												JOptionPane.DEFAULT_OPTION,
												JOptionPane.QUESTION_MESSAGE, 
												null, options, options[0]);
		if(type==3) { //if cancel is chosen
			JOptionPane.showMessageDialog(frame,  "Adding item cancelled.", "Cancelled", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		//finally, confirm item addition, if yes then call menu's add() method
		int choice = JOptionPane.showConfirmDialog(frame, "Confirm to add:\n\t- " + name + "\n\t- P" + price + "?", "Confirm", JOptionPane.YES_NO_OPTION);
		if(choice == 0) {
			try {
				Server.menu.add(name, price, (byte)type);
				JOptionPane.showMessageDialog(frame, "Added \"" + name + "\" succesfully!", "Successful", JOptionPane.INFORMATION_MESSAGE);
			}catch (IOException e) {
				JOptionPane.showMessageDialog(frame, "Can't add the item at the moment...", "Failed", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(frame,  "Adding item cancelled.", "Cancelled", JOptionPane.WARNING_MESSAGE);
			return;
		}
	}
	
	/**
	 * Add an item to the menu by directly passing an Item object
	 */
	public static void addItem(JFrame frame, String name, double price, byte type) {
		try {
			Server.menu.add(name, price, (byte)type);
			JOptionPane.showMessageDialog(frame, "Added \"" + name + "\" succesfully!", "Successful", JOptionPane.INFORMATION_MESSAGE);
		}catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "Can't add the item at the moment...", "Failed", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Removes an item from GlobalComponent.menu
	 * Ask for an input then validate if it's an existing
	 * itemID from the menu. If the input is valid
	 * pass it to menu's remove() method.
	 */
	public static void removeItem(JFrame frame) {
		
		String itemID = JOptionPane.showInputDialog(frame, "Enter Item ID to remove: ", "Removing Item", JOptionPane.QUESTION_MESSAGE);
		Menu menu = Server.menu;
		
		//if user cancels
		if(itemID==null || itemID.isBlank()) {
			JOptionPane.showMessageDialog(frame,  "Remove item cancelled.", "Cancelled", JOptionPane.WARNING_MESSAGE);
		//if user enters a valid item id
		} else if (menu.getMenu().containsKey(itemID.toUpperCase())) {
			//get item name
			String name = menu.getMenu().get(itemID.toUpperCase()).getName();
			//confirm item removal
			int confirm = JOptionPane.showConfirmDialog(frame, "Comfirm to remove " + itemID.toUpperCase() + " - " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION);
			if(confirm==0) {
				String removed = Server.menu.getMenu().get(itemID.toUpperCase()).getName();
				try {
					Server.menu.remove(itemID.toUpperCase());
					JOptionPane.showMessageDialog(frame, "Removed \"" + removed + "\" succesfully!", "Successful", JOptionPane.INFORMATION_MESSAGE);
				} catch(IOException e) {
					JOptionPane.showMessageDialog(frame, "Can't remove the item at the moment...", "Failed", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(frame,  "Remove item cancelled.", "Cancelled", JOptionPane.WARNING_MESSAGE);
			}
		//if user enters an invalid item id
		} else {
			JOptionPane.showMessageDialog(frame,  "You entered an invalid ID", "Invalid", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * Remove an item by directly passing an item ID
	 */
	public static void removeItem(JFrame frame, String itemID) {
		String removed = Server.menu.getMenu().get(itemID.toUpperCase()).getName();
		try {
			Server.menu.remove(itemID.toUpperCase());
			JOptionPane.showMessageDialog(frame, "Removed \"" + removed + "\" succesfully!", "Successful", JOptionPane.INFORMATION_MESSAGE);
		} catch(IOException e) {
			JOptionPane.showMessageDialog(frame, "Can't remove the item at the moment...", "Failed", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Edits an existing item from GlobalComponent.menu
	 * Asks for an existing item-id, then ask for new
	 * name and new price. Then pass it to the menu's
	 * edit method.
	 */
	public static void editItem(JFrame frame) {
		
		String itemID = JOptionPane.showInputDialog(frame, "Enter Item ID to edit: ", "Editting Item", JOptionPane.QUESTION_MESSAGE);
		Menu menu = Server.menu;
		
		//if user cancels
		if(itemID==null || itemID.isBlank()) {
			JOptionPane.showMessageDialog(frame,  "Edit item cancelled.", "Cancelled", JOptionPane.WARNING_MESSAGE);
		//if user enters a valid item id
		}  else if (menu.getMenu().containsKey(itemID.toUpperCase())) {
			
			//getting current details of item to edit
			Item item = menu.getMenu().get(itemID.toUpperCase());
			String oldName = item.getName();
			double oldPrice = item.getPrice();
			
			//asking for new item name
			String newName = JOptionPane.showInputDialog(frame, "Enter new name:\n(previously " + oldName + ")", "Editting " + itemID.toUpperCase(), JOptionPane.QUESTION_MESSAGE );
			//asking for new item price, repeats if invalid
			double newPrice = 0;
			while(true) {
				String sPrice = JOptionPane.showInputDialog(frame, "Enter new name:\n(previously P" + oldPrice + ")", "Editting " + itemID.toUpperCase(), JOptionPane.QUESTION_MESSAGE );
				try {
					newPrice = Double.parseDouble(sPrice);
					break;
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(frame, "Invalid price!", "Invalid", JOptionPane.WARNING_MESSAGE);
				}
			}
			
			//confirm item edit
			int confirm = JOptionPane.showConfirmDialog(frame, "Comfirm to edit \"" + itemID.toUpperCase() + "\":"
														+ "\n\t - Item Name  : " + oldName + " to " + newName
														+ "\n\t - Item Price : P" + oldPrice + " to P" + newPrice, 
														"Confirm", JOptionPane.YES_NO_OPTION);
			//if edit is confirmed, try to edit the item
			if(confirm==0) {
				try {
					Server.menu.edit(itemID.toUpperCase(), newName, newPrice);
					JOptionPane.showMessageDialog(frame, "Editted \"" + itemID.toUpperCase() + "\" succesfully!", "Successful", JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frame, "Can't edit the item at the moment...", "Failed", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(frame,  "Edit item cancelled.", "Cancelled", JOptionPane.WARNING_MESSAGE);
			}
			
		//if user enters an invalid item id
		} else {
			JOptionPane.showMessageDialog(frame,  "You entered an invalid ID", "Invalid", JOptionPane.WARNING_MESSAGE);
		}
		
	}
	
	/**
	 * Edits an item by directly passing the new values
	 */
	public static void editItem(JFrame frame, String itemID, String newName, double newPrice) {
		try {
			Server.menu.edit(itemID.toUpperCase(), newName, newPrice);
			JOptionPane.showMessageDialog(frame, "Editted \"" + itemID.toUpperCase() + "\" succesfully!", "Successful", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "Can't edit the item at the moment...", "Failed", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Call this method to process an order with status "waiting"
	 * Enter orderNo and the status will be set to "processing"
	 */
	public static void processOrder(JFrame frame) {
		
		//get Order no.
		String orderNo = JOptionPane.showInputDialog(frame, "Enter Order Number to process: ", "Process an Order", JOptionPane.QUESTION_MESSAGE);
		//user cancels
		if(orderNo==null || orderNo.isBlank()) {
			JOptionPane.showMessageDialog(frame, "You cancelled order processing...", "Cancelled", JOptionPane.WARNING_MESSAGE);
			return;
		//user enters invalid order number
		} else if(!Server.orders.containsKey(Integer.parseInt(orderNo))) {
			JOptionPane.showMessageDialog(frame, "Can't find order number...", "Cancelled", JOptionPane.ERROR_MESSAGE);
			return;
		//user enters a valid order number, with a valid previous order status
		} else if(Server.orders.get(Integer.parseInt(orderNo)).getStatus().equals("WAITING")) {
			//pass to announcer sender
			Server.at.sendToAnnouncer(frame, "PROCESS", orderNo);
		} else {
			JOptionPane.showMessageDialog(frame, "Order status can't skip!\n(Waiting -> Processing -> Serving -> Completed)", "Can't skip.", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Call this method to serve an order with status "processing"
	 * Enter orderNo and the status will be set to "serving"
	 */
	public static void serveOrder(JFrame frame) {
		
		//get Order no.
		String orderNo = JOptionPane.showInputDialog(frame, "Enter Order Number to Serve: ", "Serve an Order", JOptionPane.QUESTION_MESSAGE);
		//user cancels
		if(orderNo==null || orderNo.isBlank()) {
			JOptionPane.showMessageDialog(frame, "You cancelled order serving...", "Cancelled", JOptionPane.WARNING_MESSAGE);
			return;
		//user enters invalid order number
		} else if(!Server.orders.containsKey(Integer.parseInt(orderNo))) {
			JOptionPane.showMessageDialog(frame, "Can't find order number...", "Cancelled", JOptionPane.ERROR_MESSAGE);
			return;
		//user enters a valid order number, with a valid previous order status
		} else if(Server.orders.get(Integer.parseInt(orderNo)).getStatus().equals("PROCESSING")){
			//pass to announcer sender
			Server.at.sendToAnnouncer(frame, "SERVE", orderNo);
		} else {
			JOptionPane.showMessageDialog(frame, "Order status can't skip!\n(Waiting -> Processing -> Serving -> Completed)", "Can't skip.", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Call this method to complete an order with status "serving"
	 * Enter orderNo and the status will be set to "completed"
	 */
	public static void completeOrder(JFrame frame) {
		
		//get Order no.
		String orderNo = JOptionPane.showInputDialog(frame, "Enter Order Number to Complete: ", "Complete an Order", JOptionPane.QUESTION_MESSAGE);
		//user cancels
		if(orderNo==null || orderNo.isBlank()) {
			JOptionPane.showMessageDialog(frame, "You cancelled order completion...", "Cancelled", JOptionPane.WARNING_MESSAGE);
			return;
		//user enters invalid order number
		} else if(!Server.orders.containsKey(Integer.parseInt(orderNo))) {
			JOptionPane.showMessageDialog(frame, "Can't find order number...", "Cancelled", JOptionPane.ERROR_MESSAGE);
			return;
		//user enters a valid order number, with a valid previous order status
		} else if(Server.orders.get(Integer.parseInt(orderNo)).getStatus().equals("SERVING")){
			//pass to announcer sender
			Server.at.sendToAnnouncer(frame, "COMPLETE", orderNo);
		} else {
			JOptionPane.showMessageDialog(frame, "Order status can't skip!\n(Waiting -> Processing -> Serving -> Completed)", "Can't skip.", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * returns an arrayList of all orders except completed orders
	 */
	public static ArrayList<Order> refreshOrder() {
		
		ArrayList<Order> result = new ArrayList<Order>();
		
		for(Order order: Server.orders.values()) {
			if(!order.getStatus().equalsIgnoreCase("COMPLETED"))
				result.add(order);
		}
		
		return result;
	}
	
	
	/**
	 * returns an arrayList of filtered orders according to their status
	 * valid arguments to pass are the ones above
	 */
	public static ArrayList<Order> refreshOrder(int filter) {
		
		ArrayList<Order> result = new ArrayList<Order>();
		
		for(Order order: Server.orders.values()) {
			switch(filter) {
			case STATUS_WAITING:
				if(order.getStatus().equalsIgnoreCase("WAITING"))
					result.add(order);
				break;
			case STATUS_PROCESSING:
				if(order.getStatus().equalsIgnoreCase("PROCESSING"))
					result.add(order);
				break;
			case STATUS_SERVING:
				if(order.getStatus().equalsIgnoreCase("SERVING"))
					result.add(order);
				break;
			case STATUS_COMPLETED:
				if(order.getStatus().equalsIgnoreCase("COMPLETED"))
					result.add(order);
				break;
			default:
				return refreshOrder();	//if the filter is not valid, return unfiltered orders
			}
		}
		
		return result;
	}

	/**
	 * Starts the 3 threads that manages the servers
	 */
	static void openServers(JFrame frame) {
		Server.st = new OrderThread();
		Server.mt = new MenuThread();
		Server.at = new AnnouncerThread(frame);
		Server.st.start();
		Server.mt.start();
		Server.at.start();
		
	}
	
	/**
	 * closes the 3 servers by interrupting the threads
	 * that manage them
	 */
	static void closeServers() {
		Server.st.interrupt();
		Server.mt.interrupt();
		Server.at.interrupt();
	}
}
