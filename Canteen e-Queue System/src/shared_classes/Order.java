package shared_classes;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class is used to create Order objects. Orders objects are
 * created in Client applications, then submitted to socket server
 * to be processed
 */
public class Order implements Serializable{
	
	private static final long serialVersionUID = -6408286039369306142L;
	private int orderNo;													//to be requested from Server app
	private HashMap<Item, Integer> items = new HashMap<Item, Integer>();	//Items (item) and Integer (quantity)
	private String name; 													//Name of orderer
	private double total;													//total price of all the items
	private String status = "WAITING";										//status of the Order
	
	public Order(String name){
		this.name = name;
	}
	//getters
	public int getOrderNo() {
		return this.orderNo;
	}
	public HashMap<Item, Integer> getItems() {
		return items;
	}
	public String getName() {
		return this.name;
	}
	public double getTotal() {
		return this.total;
	}
	public String getStatus() {
		return this.status;
	}
	//setters
	public void setOrderNo(int orderNo) {
		if(this.orderNo == 0)//we don't want to modify orderNo more than once.
			this.orderNo = orderNo;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * only accepts valid Statuses, do nothing if argument is not valid.
	 * Valid arguments are:
	 *  - PROCESSING
	 *  - SERVING
	 *  - COMPLETED
	 */
	public void setStatus(String status) {
		status = status.toUpperCase();
		if(status.equals("PROCESSING") || status.equals("SERVING") || status.equals("COMPLETED")) {
			this.status = status;
		}
	}
	/**
	 * no setters for items and total
	 * those should only be modified
	 * by the add() and remove()
	 * methods
	 */
	public void add(Item item, int quantity) {
		if(quantity>0) {
			this.items.put(item, quantity);				//add it to HashMap
			this.total += item.getPrice() * quantity;	//Update total price of order
		}
	}
	public void remove(Item item) {
		this.total = total - (item.getPrice() * this.items.get(item));
		this.items.remove(item);
	}
	
	/**
	 * Checks if the order is empty
	 */
	public boolean isEmpty() {
		return this.items.isEmpty();
	}
	
	/**
	 * Check if order contains an item
	 */
	public boolean contains(Item item) {
		return this.items.keySet().contains(item);
	}
	
	//get a listed formatted contents of the order
	public String getContents() {
		
		StringBuilder sb = new StringBuilder();
		
		for(Item item: items.keySet()) {
			sb.append(item.getName() + " x" + items.get(item) + "<br>\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * This method turns the infos of the order into a sentence form
	 * Good to be used for logs
	 */
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(this.name + " ordered: ");
		
		if(!items.isEmpty()) {
			for(Item item: items.keySet())									//Iterate through the HashMap
				sb.append(item.getName() + " x" + items.get(item) + ", ");	//gets the name of current item and it's quantity
			sb.append("for a total of P" + this.getTotal());				//append total price
		}else {
			sb.append("nothing.");											//say Nothing, if order is empty
		}
		
		return sb.toString();
	}
}
