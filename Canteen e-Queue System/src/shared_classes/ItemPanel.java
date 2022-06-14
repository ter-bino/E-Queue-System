package shared_classes;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.border.LineBorder;

/**
 * Each instance of this class is a panel
 * for a specific item in the displayed Menu
 * in the Client app and in the Server app.
 */
public class ItemPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	//Font used in the whole GUI
	static Font font = new Font("Verdana Pro Cond Black", Font.BOLD, 32);
	
	//instance variables
	private ImageIcon icon;
	private JLabel category, itemID, itemName, itemPrice;
	

	/**
	 * Create the panel.
	 */
	public ItemPanel(Item item) {
		
		String html;	//helps in formatting
		
		setBorder(new LineBorder(Color.WHITE, 1, true));
		setBackground(Color.LIGHT_GRAY);
		setToolTipText(item.toString());
		setLayout(new GridBagLayout());
		
		//Choose proper logo for this ItemPanel
		switch(item.getCategory().toUpperCase()) {
		case "MEAL":
			icon = new ImageIcon(new ImageIcon(ItemPanel.class.getResource("/shared_classes/Meal.png")).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
			break;
		case "BEVERAGE":
			icon = new ImageIcon(new ImageIcon(ItemPanel.class.getResource("/shared_classes/Beverage.png")).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
			break;
		case "DESSERT":
			icon = new ImageIcon(new ImageIcon(ItemPanel.class.getResource("/shared_classes/Dessert.png")).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
			break;
		}
		
		//Category label
		html = "<html><body width='150px'>" + item.getCategory() + "</body></html>";
		category = new JLabel(html);
		category.setIcon(icon);
		category.setHorizontalAlignment(JLabel.LEFT);
		category.setFont(font);
		GridBagConstraints gbc_lblCategory = new GridBagConstraints();
		gbc_lblCategory.ipadx = 5;
		gbc_lblCategory.ipady = 5;
		gbc_lblCategory.weightx = 0.15;
		gbc_lblCategory.anchor = GridBagConstraints.WEST;
		gbc_lblCategory.fill = GridBagConstraints.BOTH;
		gbc_lblCategory.gridx = 0;
		gbc_lblCategory.gridy = 0;
		add(category, gbc_lblCategory);
		
		//itemID label
		html = "<html><body width='150px'>" + item.getItemID() + "</body></html>";
		itemID = new JLabel(html);
		itemID.setHorizontalAlignment(JLabel.LEFT);
		itemID.setFont(font);
		itemID.setForeground(Color.BLACK);
		GridBagConstraints gbc_lblItemID = new GridBagConstraints();
		gbc_lblItemID.ipadx = 5;
		gbc_lblItemID.ipady = 5;
		gbc_lblItemID.weightx = 0.15;
		gbc_lblItemID.anchor = GridBagConstraints.WEST;
		gbc_lblItemID.fill = GridBagConstraints.BOTH;
		gbc_lblItemID.gridx = 1;
		gbc_lblItemID.gridy = 0;
		add(itemID, gbc_lblItemID);
		
		//itenName label
		html = "<html><body width='375px'>" + item.getName() + "</body></html>";
		itemName = new JLabel();
		itemName.setText(html);
		itemName.setHorizontalAlignment(JLabel.LEFT);
		itemName.setFont(font);
		itemName.setForeground(Color.BLACK);
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.ipadx = 5;
		gbc_lblName.ipady = 5;
		gbc_lblName.weightx = 0.45;
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.fill = GridBagConstraints.BOTH;
		gbc_lblName.gridx = 2;
		gbc_lblName.gridy = 0;
		add(itemName, gbc_lblName);
		
		//itemPrice label
		html = "<html><body width='225px'>P" + item.getPrice() + "</body></html>";
		itemPrice = new JLabel();
		itemPrice.setText(html);
		itemPrice.setHorizontalAlignment(JLabel.LEFT);
		itemPrice.setFont(font);
		itemPrice.setForeground(Color.BLACK);
		GridBagConstraints gbc_lblPrice = new GridBagConstraints();
		gbc_lblPrice.ipadx = 5;
		gbc_lblPrice.ipady = 5;
		gbc_lblPrice.weightx = 0.25;
		gbc_lblPrice.anchor = GridBagConstraints.WEST;
		gbc_lblPrice.fill = GridBagConstraints.BOTH;
		gbc_lblPrice.gridx = 3;
		gbc_lblPrice.gridy = 0;
		add(itemPrice, gbc_lblPrice);	
	}
}
