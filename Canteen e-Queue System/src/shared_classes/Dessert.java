package shared_classes;

import java.io.IOException;
import java.io.Serializable;

/**
 * This method was unnecessary, yet. For possible future
 * plans of adding other attributes like Size and Flavor
 */
public class Dessert extends Item implements Serializable{

	private static final long serialVersionUID = 1907998047612173799L;

	public Dessert(String name, double price) throws IOException {
		super(name, price, setID("dessert"));
		this.setCategory("Dessert");
	}

}
