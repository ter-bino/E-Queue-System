package shared_classes;

import java.io.IOException;
import java.io.Serializable;

/**
 * This method was unnecessary, yet. For possible future
 * plans of adding other attributes like Size and Flavor
 * @author alter
 */
public class Beverage extends Item implements Serializable{

	private static final long serialVersionUID = -8480499699194262464L;
	
	
	/**
	 * 
	 * @param name - name of the item
	 * @param price - price of the item
	 * @throws IOException
	 */
	public Beverage(String name, double price) throws IOException {
		super(name, price, setID("beverage"));
		this.setCategory("Beverage");
	}

}
