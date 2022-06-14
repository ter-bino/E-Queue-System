package equeue_client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import shared_classes.Item;
import shared_classes.Order;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EtchedBorder;
import javax.swing.JSeparator;

/**
 * This class is the main class of the Client application.
 * Creates a connection with the server to send orders and
 * display the number assigned by the server to the order
 * that was sent.
 */
public class Client extends JFrame {

	static private final long serialVersionUID = 1L;
	
	//To make the application full screen
	static private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	static Order order;													//One and only order object in this application
	static HashMap<String, Item> menu;									//The menu which is constantly updated from the server
	
	static String host;													//server host address, to be asked from user at lunch
	static final public int SERVER_PORT = 8888;							//server port - order exchange
	static final int MENU_PORT = 8889;									//server port - menu fetching
	
	static private JPanel contentPane;
	
	static Client frame;												//the main frame of the application

	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			//just proceed with default LAF
		}
		
		getServer();						//ask for server host address
		
		ClientFunctions.checkServer();		//fetch menu from server
		
		
		//lunch the client app
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Client();
					updateContent();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	//instance variavles of Client()
	private JPanel headerPanel, buttonsPanel, centerPanel, menuPanel, columnHeader, rightSpacing, filterPanel;
	private JButton btnStart, btnAdd, btnRemove, btnCheck, btnCancel, btnSend, btnFullMenu, btnMeals, btnBeverages, btnDesserts;
	private JLabel titleLabel, lblCategory, lblItemID, lblName, lblPrice, lblFilter, lblUseInstruction, lblStartOrder, lblAddItems, lblCheckOrder, lblSendOrder;
	private JScrollPane scrollPane;
	private JSeparator separator;
	
	/***
	 * Create the frame.
	 */
	public Client() {
		setTitle("E-Queue System: Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1280, 720));
		setPreferredSize(new Dimension(screenSize.width, screenSize.height));
		setMaximumSize(new Dimension(screenSize.width, screenSize.height));
		setSize(screenSize.width, screenSize.height);	//for official
		//setBounds(100, 100, 1280, 720);				//for test
		setUndecorated(true);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 3, true));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);
		
		//header
		headerPanel = new JPanel();
		headerPanel.setBackground(Color.DARK_GRAY);
		headerPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		headerPanel.setLayout(new FlowLayout());
		contentPane.add(headerPanel, BorderLayout.NORTH);
		FlowLayout fl_headerPanel = new FlowLayout(FlowLayout.CENTER, 0, 0);
		fl_headerPanel.setAlignOnBaseline(true);
		headerPanel.setLayout(fl_headerPanel);
		
		//title
		titleLabel = new JLabel("Canteen E-Queue System");
		titleLabel.setForeground(Color.RED);
		titleLabel.setFont(new Font("Verdana Pro Cond Black", Font.BOLD, 38));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBackground(Color.DARK_GRAY);
		titleLabel.setIcon(new ImageIcon(new ImageIcon(Client.class.getResource("/shared_classes/Logo.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
		headerPanel.add(titleLabel);
		
		//panel of buttons
		buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Color.LIGHT_GRAY);
		buttonsPanel.setLayout(new GridLayout(1,6));
		GridLayout gridLayout_1 = (GridLayout) buttonsPanel.getLayout();
		gridLayout_1.setVgap(50);
		gridLayout_1.setHgap(50);
		contentPane.add(buttonsPanel, BorderLayout.SOUTH);
		
		//startanorder button
		btnStart = new JButton("START AN ORDER");
		btnStart.setToolTipText("Starts an order for you.");
		btnStart.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnStart.setForeground(Color.WHITE);
		btnStart.setBackground(Color.BLACK);
		buttonsPanel.add(btnStart);
		btnStart.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					ClientFunctions.startOrder(frame);
					btnStart.setEnabled(false);
				} catch (ClassNotFoundException | IOException e1) {
					//handle the exception with a diss
					JOptionPane.showMessageDialog(frame, "That's a lot of damage...", "Damage", JOptionPane.ERROR_MESSAGE);
				}
				
			}});
		
		//addanitem button
		btnAdd = new JButton("ADD AN ITEM");
		btnAdd.setToolTipText("Adds an item to your order.");
		btnAdd.setForeground(Color.WHITE);
		btnAdd.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnAdd.setBackground(Color.BLACK);
		buttonsPanel.add(btnAdd);
		btnAdd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ClientFunctions.addOrder(frame);
				
			}});
		
		//removeanitem button
		btnRemove = new JButton("REMOVE AN ITEM");
		btnRemove.setToolTipText("Removes an item from your order.");
		btnRemove.setForeground(Color.WHITE);
		btnRemove.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnRemove.setBackground(Color.BLACK);
		buttonsPanel.add(btnRemove);
		btnRemove.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ClientFunctions.removeOrder(frame);
				
			}});
		
		//check order button
		btnCheck = new JButton("CHECK ORDER");
		btnCheck.setToolTipText("Checks the contents of your order.");
		btnCheck.setForeground(Color.WHITE);
		btnCheck.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnCheck.setBackground(Color.BLACK);
		buttonsPanel.add(btnCheck);
		btnCheck.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ClientFunctions.checkOrder(frame);
				
			}});
		
		//cancel order button
		btnCancel = new JButton("CANCEL ORDER");
		btnCancel.setToolTipText("Empties and cancels the current order.");
		btnCancel.setForeground(Color.WHITE);
		btnCancel.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnCancel.setBackground(Color.BLACK);
		buttonsPanel.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientFunctions.cancelOrder(frame);
				btnStart.setEnabled(true);
			}});
		
		//send order button
		btnSend = new JButton("SEND ORDER");
		btnSend.setToolTipText("Send the contents of the current order to the server.");
		btnSend.setForeground(Color.WHITE);
		btnSend.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnSend.setBackground(Color.BLACK);
		buttonsPanel.add(btnSend);
		btnSend.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ClientFunctions.sendOrder(frame);
				if(order==null)
					btnStart.setEnabled(true);
			}});
		
		//panel in the center
		centerPanel = new JPanel();
		centerPanel.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));
		
		//panel that contains the menu
		menuPanel = new JPanel();
		menuPanel.setBackground(Color.DARK_GRAY);
		
		//scroller of the menu panel
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(menuPanel);
		menuPanel.setLayout(new GridBagLayout());
		centerPanel.add(scrollPane);
		
		//column headers of the menu
		columnHeader = new JPanel();
		centerPanel.add(columnHeader, BorderLayout.NORTH);
		columnHeader.setLayout(new GridBagLayout());

		//category label in the column header
		lblCategory = new JLabel("Category");
		lblCategory.setToolTipText("Meal, beverage or dessert");
		GridBagConstraints gbc_lblCategory = new GridBagConstraints();
		gbc_lblCategory.ipadx = 5;
		gbc_lblCategory.ipady = 5;
		gbc_lblCategory.weightx = 0.25;
		gbc_lblCategory.anchor = GridBagConstraints.WEST;
		gbc_lblCategory.gridx = 0;
		gbc_lblCategory.gridy = 0;
		columnHeader.add(lblCategory, gbc_lblCategory);

		//item id label in the column header
		lblItemID = new JLabel("Item ID");
		lblItemID.setToolTipText("You need this Item ID to add or remove an item in your order");
		GridBagConstraints gbc_lblItemID = new GridBagConstraints();
		gbc_lblItemID.ipadx = 5;
		gbc_lblItemID.ipady = 5;
		gbc_lblItemID.weightx = 0.25;
		gbc_lblItemID.anchor = GridBagConstraints.WEST;
		gbc_lblItemID.gridx = 1;
		gbc_lblItemID.gridy = 0;
		columnHeader.add(lblItemID, gbc_lblItemID);

		//name label in the column header
		lblName = new JLabel("Item Name");
		lblName.setToolTipText("Name of the item");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.ipadx = 5;
		gbc_lblName.ipady = 5;
		gbc_lblName.weightx = 0.5;
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.gridx = 2;
		gbc_lblName.gridy = 0;
		columnHeader.add(lblName, gbc_lblName);
		
		//price label in the column header
		lblPrice = new JLabel("Item Price");
		lblPrice.setToolTipText("The price of the item");
		GridBagConstraints gbc_lblPrice = new GridBagConstraints();
		gbc_lblItemID.ipadx = 5;
		gbc_lblItemID.ipady = 5;
		gbc_lblPrice.weightx = 0.25;
		gbc_lblPrice.anchor = GridBagConstraints.WEST;
		gbc_lblPrice.gridx = 3;
		gbc_lblPrice.gridy = 0;
		columnHeader.add(lblPrice, gbc_lblPrice);
		
		//Spacers in the East of the main BorderLayout() or main JFrame
		rightSpacing = new JPanel();
		rightSpacing.setBackground(Color.LIGHT_GRAY);
		contentPane.add(rightSpacing, BorderLayout.EAST);
		
		//panel with filter and instructions
		filterPanel = new JPanel();
		filterPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.DARK_GRAY, Color.DARK_GRAY));
		filterPanel.setBackground(Color.GRAY);
		contentPane.add(filterPanel, BorderLayout.WEST);
		filterPanel.setLayout(new GridLayout(15, 1, 0, 0));
		
		lblFilter = new JLabel(" Menu filters:");
		lblFilter.setToolTipText("");
		lblFilter.setFont(new Font("Verdana Pro Cond Semibold", Font.BOLD, 22));
		lblFilter.setForeground(Color.BLACK);
		filterPanel.add(lblFilter);
		
		//button to show all items without filter
		btnFullMenu = new JButton("FULL MENU");
		btnFullMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateContent();
			}
		});
		btnFullMenu.setToolTipText("Shows the full menu");
		btnFullMenu.setForeground(Color.WHITE);
		btnFullMenu.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnFullMenu.setBackground(Color.BLACK);
		filterPanel.add(btnFullMenu);
		
		//button to filter all orders that are meals
		btnMeals = new JButton("MEALS");
		btnMeals.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateContent(MEAL);
			}
		});
		btnMeals.setToolTipText("Shows all \"meals\" in the menu");
		btnMeals.setForeground(Color.WHITE);
		btnMeals.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnMeals.setBackground(Color.BLACK);
		filterPanel.add(btnMeals);

		//button to filter all orders that are beverages
		btnBeverages = new JButton("BEVERAGES");
		btnBeverages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateContent(BEVERAGE);
			}
		});
		btnBeverages.setToolTipText("Shows all \"beverages\" in the menu");
		btnBeverages.setForeground(Color.WHITE);
		btnBeverages.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnBeverages.setBackground(Color.BLACK);
		filterPanel.add(btnBeverages);

		//button to filter all orders that are desserts
		btnDesserts = new JButton("DESSERTS");
		btnDesserts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateContent(DESSERT);
			}
		});
		btnDesserts.setToolTipText("Shows all \"desserts\" in the menu");
		btnDesserts.setForeground(Color.WHITE);
		btnDesserts.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnDesserts.setBackground(Color.BLACK);
		filterPanel.add(btnDesserts);
		
		separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		filterPanel.add(separator);
		
		//From here onwards, display instruction for use of program
		lblUseInstruction = new JLabel(" Use instruction:");
		lblUseInstruction.setToolTipText("");
		lblUseInstruction.setForeground(Color.BLACK);
		lblUseInstruction.setFont(new Font("Verdana", Font.PLAIN, 18));
		filterPanel.add(lblUseInstruction);
		
		lblStartOrder = new JLabel("  1) Start order");
		lblStartOrder.setToolTipText("");
		lblStartOrder.setForeground(Color.BLACK);
		lblStartOrder.setFont(new Font("Verdana", Font.PLAIN, 18));
		filterPanel.add(lblStartOrder);
		
		lblAddItems = new JLabel("  2) Add items");
		lblAddItems.setToolTipText("");
		lblAddItems.setForeground(Color.BLACK);
		lblAddItems.setFont(new Font("Verdana", Font.PLAIN, 18));
		filterPanel.add(lblAddItems);
		
		lblCheckOrder = new JLabel("  3) Check order");
		lblCheckOrder.setToolTipText("");
		lblCheckOrder.setForeground(Color.BLACK);
		lblCheckOrder.setFont(new Font("Verdana", Font.PLAIN, 18));
		filterPanel.add(lblCheckOrder);
		
		lblSendOrder = new JLabel("  4) Send order");
		lblSendOrder.setToolTipText("");
		lblSendOrder.setForeground(Color.BLACK);
		lblSendOrder.setFont(new Font("Verdana", Font.PLAIN, 18));
		filterPanel.add(lblSendOrder);
		
	}
	

	
	//method that ask for the user to input the server's ip address
	public static void getServer() {

		//loop that attempts to coonect to the server
		while(true) {
			try {
				//ask for host
				host = JOptionPane.showInputDialog("Enter server IP address:");
				//checks if host is valid to connect to, then close it right away.
				if(host!=null) {
					Socket client = new Socket(host, SERVER_PORT);
					client.close();
					break;
				} else {
					System.exit(0);
					return;
				}
			//reask for server address if connection attempt fails...
			} catch (IOException e) {
				int resp = JOptionPane.showConfirmDialog(null, "Can't connect to that address... Retry?", "Failed", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
				if(resp != 0) {
					System.exit(0);;
					return;
				}
			}
		}
	}
	
	/**
	 * Updates the content of menu without any Filter
	 * Fetches updated menu from server as well
	 * (displays all)
	 */
	static void updateContent() {
		//fetch updated menu
		ClientFunctions.checkServer();
		//clear contents of the menu before updating
		frame.menuPanel.removeAll();
		
		//formatting the content
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH; 
		
		//adding every item as a ClientItemPanel object
		for(Item item: menu.values()) {
			frame.menuPanel.add(new ClientItemPanel(item), gbc);
			gbc.gridy++;
		}
		
		//update/fix displayed content
		frame.revalidate();
		frame.repaint();
	}
	
	/**
	 * filter constants
	 */
	static final public int MEAL = 0;
	static final public int BEVERAGE = 1;
	static final public int DESSERT = 2;
	
	/**
	 * Updates content of Menu with filtering
	 *  - pass 0 for meals
	 *  - pass 1 for beverages
	 *  - pass 2 for desserts	
	 */
	static void updateContent(int filter) {
		
		//fetch updated menu
		ClientFunctions.checkServer();
		//clear contents of the menu before updating
		frame.menuPanel.removeAll();
			
		//formatting the content
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
				
		//content updater with filter
		switch(filter) {
		case MEAL:
			for(Item item: menu.values()) {
				if(item.getCategory().equalsIgnoreCase("MEAL")) {
					frame.menuPanel.add(new ClientItemPanel(item), gbc);
					gbc.gridy++;
				}
			}
			break;
		case BEVERAGE:
			for(Item item: menu.values()) {
				if(item.getCategory().equalsIgnoreCase("BEVERAGE")) {
					frame.menuPanel.add(new ClientItemPanel(item), gbc);
					gbc.gridy++;
				}
			}
			break;
		case DESSERT:
			for(Item item: menu.values()) {
				if(item.getCategory().equalsIgnoreCase("DESSERT")) {
					frame.menuPanel.add(new ClientItemPanel(item), gbc);
					gbc.gridy++;
				}
			}
			break;
		default:
			updateContent();
		}
		
		//update/fix displayed content
		frame.revalidate();
		frame.repaint();
	}
}
