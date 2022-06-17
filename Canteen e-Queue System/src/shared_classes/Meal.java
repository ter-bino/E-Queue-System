package shared_classes;

import java.io.IOException;
import java.io.Serializable;

/**
 * This method was unnecessary, yet. For possible future
 * plans of adding other attributes like Size and Flavor
 * @author alter
 */
public class Meal extends Item implements Serializable{

	private static final long serialVersionUID = 3605423725550662593L;
	
	/**
	 * @param name - name of the item
	 * @param price - price of the item
	 * @throws IOException
	 */
	public Meal(String name, double price) throws IOException {
		super(name, price, setID("meal"));
		this.setCategory("Meal");
	}

}
