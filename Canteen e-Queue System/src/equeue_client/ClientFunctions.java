package equeue_client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

//self-made packages/classes
import shared_classes.Menu;
import shared_classes.Order;
import shared_classes.Item;

/**
 * This class contains multiple STATIC methods that
 * are called when clicking on different buttons in
 * the GUI of the Client application.
 */
public class ClientFunctions {
	
	/**
	 * Checks server availability/
	 * Stucks the program in a loop if server is not available
	 * Call this method before any action that interacts with
	 * the server.
	 * (Also updates the menu)
	 */
	public static void checkServer() {
		while(true) {
			try {
				Client.menu = ClientFunctions.getMenu();
				break;
			} catch (ClassNotFoundException | IOException e) {
				JOptionPane.showMessageDialog(null,  "Can't connect to server...", "Conenction Failed", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Instantiates a new order object for the client to edit before sending to server
	 */
	public static void startOrder(JFrame frame) throws ClassNotFoundException, UnknownHostException, IOException {

		checkServer();
		Client.updateContent();
		String name = JOptionPane.showInputDialog(frame, "Enter your name: ", "Starting Order", JOptionPane.QUESTION_MESSAGE);
		if(name==null || name.isBlank())
			name = "(nameless)";
		Client.order = new Order(name);
	}
	
	/**
	 * Passes an Order object to the server,
	 * then displays the order number (int)
	 * assigned to that order.
	 */
	public static void sendOrder(JFrame frame){
		
		//if there's currently no order (call startOrder() before this method!)
		if(Client.order==null) {
			JOptionPane.showMessageDialog(frame, "Start an order first!", "No order", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//check server availability before sending order
		checkServer();
		
		//cancel if order is empty
		if(Client.order.isEmpty()) {
			JOptionPane.showMessageDialog(frame,  "Your order is empty!", "Failed to Send", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		//attempt to send the order to the server
		try {
			checkOrder(frame);
			Order order = Client.order;
	
			Socket socket = new Socket(Client.host, Client.SERVER_PORT);
			
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			Scanner in = new Scanner(socket.getInputStream());
			
			out.writeObject(order);
			
			int orderNum = Integer.parseInt(in.nextLine());
			
			new OrderNumberDialog(frame, orderNum);
	
			socket.close();
			
			Client.order = null;
		} catch(IOException e) {
			JOptionPane.showMessageDialog(frame, "Can't send order at the moment...", "Failed to send", JOptionPane.ERROR_MESSAGE);
		}
		
		
	}
	
	/**
	 * Adds an item to the order by asking for item id
	 * and quantity in a series of JOptionPane input
	 * dialogs
	 */
	public static void addOrder(JFrame frame) {
		
		//if there's currently no order (call startOrder() before this method!)
		if(Client.order==null) {
			JOptionPane.showMessageDialog(frame, "Start an order first!", "No order", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//ask for ID of item to add
		String item_id = JOptionPane.showInputDialog(frame, "Enter Item ID to add: ", "Adding to Order", JOptionPane.QUESTION_MESSAGE);
		
		//if user cancels
		if(item_id == null || item_id.isBlank()) {
			JOptionPane.showMessageDialog(frame,  "Adding to order cancelled.", "Cancelled", JOptionPane.WARNING_MESSAGE);
			return;
		//if user inputs invalid id
		} else if(!Client.menu.keySet().contains(item_id.toUpperCase())) {
			JOptionPane.showMessageDialog(frame,  "You entered an invalid Item ID!", "Invalid", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		//asking for quantity, keep repeating if invalid, break if input is valid or user cancels
		int quantity;
		while(true) {
			String sQuantity = JOptionPane.showInputDialog(frame, Client.menu.get(item_id.toUpperCase()).toString() + "\n - Enter quantity to add: ", "Adding to Order", JOptionPane.QUESTION_MESSAGE);
			if(sQuantity == null) {
				JOptionPane.showMessageDialog(frame,  "Adding to order cancelled.", "Cancelled", JOptionPane.WARNING_MESSAGE);
				return;
			}
			try {
				quantity = Integer.parseInt(sQuantity);
				break;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(frame,  "You entered an invalid quantity!", "Invalid", JOptionPane.WARNING_MESSAGE);
			}
		}
		Client.order.add(Client.menu.get(item_id.toUpperCase()), quantity);
		JOptionPane.showMessageDialog(frame,  "Item added to order successfully", "Successful", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Removes an item from the order by asking
	 * for the id of the item to remove
	 */
	public static void removeOrder(JFrame frame) {
		
		//if there's currently no order (call startOrder() before this method)
		if(Client.order==null) {
			JOptionPane.showMessageDialog(frame, "Start an order first!", "No order", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//ask for id of item to remove
		String item_id = JOptionPane.showInputDialog(frame, "Enter Item ID to remove: ", "Removing from Order", JOptionPane.QUESTION_MESSAGE);
		//if user clicks cancel
		if(item_id == null || item_id.isBlank()) {
			JOptionPane.showMessageDialog(frame,  "Removing from order cancelled.", "Cancelled", JOptionPane.WARNING_MESSAGE);
			return;
		//cancel if the order does not contain the item
		} else if (!Client.order.contains(Client.menu.get(item_id.toUpperCase()))) {
			JOptionPane.showMessageDialog(frame,  "The order does not contain that item.", "Failed", JOptionPane.WARNING_MESSAGE);
			return;
		}
		String itemName = Client.menu.get(item_id.toUpperCase()).getName();
		Client.order.remove(Client.menu.get(item_id.toUpperCase()));
		JOptionPane.showMessageDialog(frame,  itemName + " removed from order successfully", "Successful", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Shows the current contents of the order
	 */
	public static void checkOrder(JFrame frame) {
		
		//if there's currently no order (call startOrder() before this method)
		if(Client.order==null) {
			JOptionPane.showMessageDialog(frame, "Start an order first!", "No order", JOptionPane.ERROR_MESSAGE);
			return;
		}else {
			new OrderCheckDialog(frame);
		}
		
	}
	
	/**
	 * just cancels the order by setting
	 * the order object to null
	 */
	public static void cancelOrder(JFrame frame) {
		//if there's currently no order (call startOrder() before this method)
		if(Client.order==null) {
			JOptionPane.showMessageDialog(frame, "Start an order first!", "No order", JOptionPane.ERROR_MESSAGE);
			return;
		//else cancel the order
		}else {
			JOptionPane.showMessageDialog(frame, "You cancelled your existing order.", "No order", JOptionPane.INFORMATION_MESSAGE);
			Client.order = null;
		}
	}
	
	/**
	 * fetches updated Menu from the server
	 */
	public static HashMap<String, Item> getMenu() throws ClassNotFoundException, UnknownHostException, IOException {
		
		Menu menu;
		
		try(
			Socket socket = new Socket(Client.host, Client.MENU_PORT);
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		){
			menu = (Menu) in.readObject();
		}
	
		return menu.getMenu();
	}
}
