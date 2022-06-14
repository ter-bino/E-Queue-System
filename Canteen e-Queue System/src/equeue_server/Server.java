package equeue_server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import shared_classes.Item;
import shared_classes.Menu;
import shared_classes.Order;
import shared_classes.OrderPanel;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Image;
import javax.swing.SwingConstants;
import equeue_client.Client;
import javax.swing.ImageIcon;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Thsi is the Server application. You must run this application 
 * first to get the server address. Then you enter that server
 * address in the Client app and Announcer app to create a connection.
 * (Requires LAN connection)
 * Note:
 *     - multiple client apps can connect to server
 *     - only ONE announcer app can connect to server
 */
public class Server extends JFrame {

	private static final long serialVersionUID = 1L;
	
	//for setting the app to fullscreen
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	static int order_number = 0;	//tracks the most recent order no.
	static OrderThread st;			//Thread of running server
	static MenuThread mt;			//Server that provides Menu to clients
	static AnnouncerThread at;		//Server that sends order number + status to announcer
	
	static HashMap<Integer, Order> orders = new HashMap<Integer, Order>();	//Collection to hold existing orders
	
	static private JPanel contentPane;									//holds the contents of the program

	//Files to be located
	static final File ID_FILE = new File("item.dat");					//File for keeping track of Item IDs (prevent duplicates)
	static final File MENU_FILE = new File("menu.obj");					//The file where Menu object is stored

	static String host = "localhost";									//server host address
	//Ports to be used
	static final int SERVER_PORT = 8888;								//For server that exchanges orders
	static final int MENU_PORT = 8889;									//For server that exchanges menu
	static final int ANNOUNCE_PORT = 8890;								//For server that announces order status
	
	static Menu menu;													//Menu used by the entire interactive system
	
	static Server frame;												//the main frame of the Server application

	public static void main(String[] args)  {
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// just proceed with default LAF
		}
		
		//try to get local IP address of device in LAN
		try {
			getLocalIP();
		} catch (SocketException e1) {
			JOptionPane.showMessageDialog(frame, "Can't get LAN connection details.. Server opened in " + host, "Server Address", JOptionPane.INFORMATION_MESSAGE);
		}
		
		//open the server address in local IP or in localhost
		ServerFunctions.openServers(frame);
		
		ServerFunctions.loadMenu();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Server();
					updateMenu();
					updateOrders();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//Instance variables of this class
	private JPanel headerPanel, switcherPanel, switcherButtons, switchablePanel, filterPanel,
				menuViewPanel, menuNorthSpacer, menuWestSpacer, menuEastSpacer, menuBtnsPanel, menuCenterPanel, menuPanel, menuColumnHeader,
				ordersViewPanel, orderNorthSpacer, orderWestSpacer, orderEastSpacer, orderBtnsPanel, orderCenterPanel, orderPanel, orderColumnHeader,
				menuFilters, orderFilters;
	private JButton btnFullMenu, btnOrders, btnAddItem, btnRemoveItem, btnEditItem, btnRefresh, btnProcess, btnServe, btnComplete, btnAllItems,
					btnBeverages, btnMeals, btnDesserts, btnAllOrders, btnWaiting, btnProcessing, btnServing, btnCompleted;
	private JLabel titleLabel, lblPanels, lblCategory, lblItemID, lblName, lblPrice, lblOrderNo, lblOrderContents, lblOrderStatus, lblTotal;
	private JScrollPane menuScrollPane, orderScrollPane;
	

	/**
	 * Create the frame.
	 */
	public Server() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("E-Queue System: Server");
		setMinimumSize(new Dimension(1280, 720));
		setPreferredSize(new Dimension(screenSize.width, screenSize.height));
		setMaximumSize(new Dimension(screenSize.width, screenSize.height));
		setSize(screenSize.width, screenSize.height);	//for official
		//setBounds(100, 100, 1280, 720);				//for test
		setUndecorated(true);
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(Color.BLACK, 2));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		headerPanel = new JPanel();
		headerPanel.setBorder(new LineBorder(Color.BLACK, 2, true));
		headerPanel.setBackground(Color.DARK_GRAY);
		contentPane.add(headerPanel, BorderLayout.NORTH);
		headerPanel.setLayout(new FlowLayout());
		
		titleLabel = new JLabel("Canteen E-Queue System");
		titleLabel.setForeground(Color.RED);
		titleLabel.setIcon(new ImageIcon(new ImageIcon(Client.class.getResource("/shared_classes/Logo.png")).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("Verdana Pro Cond Black", Font.BOLD, 38));
		titleLabel.setBackground(Color.DARK_GRAY);
		headerPanel.add(titleLabel);
		
		switcherPanel = new JPanel();
		switcherPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.DARK_GRAY, Color.DARK_GRAY));
		switcherPanel.setBackground(Color.GRAY);
		contentPane.add(switcherPanel, BorderLayout.WEST);
		switcherPanel.setLayout(new GridLayout(2, 0, 20, 0));
		
		switcherButtons = new JPanel();
		switcherButtons.setBackground(Color.GRAY);
		switcherPanel.add(switcherButtons);
		switcherButtons.setLayout(new GridLayout(5, 0, 20, 50));
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		switcherButtons.add(separator);
		
		lblPanels = new JLabel("Panels:");
		lblPanels.setForeground(Color.BLACK);
		lblPanels.setFont(new Font("Verdana Pro Black", Font.PLAIN, 18));
		switcherButtons.add(lblPanels);
		
		btnFullMenu = new JButton("FULL MENU");
		btnFullMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) switchablePanel.getLayout();
				cl.show(switchablePanel, "MENU_VIEW");
				CardLayout cl2 = (CardLayout) filterPanel.getLayout();
				cl2.show(filterPanel, "MENU_FILTER");
			}
		});
		btnFullMenu.setToolTipText("View the currently existing menu");
		btnFullMenu.setForeground(Color.WHITE);
		btnFullMenu.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnFullMenu.setBackground(Color.BLACK);
		switcherButtons.add(btnFullMenu);
		
		btnOrders = new JButton("ORDER LIST");
		btnOrders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) switchablePanel.getLayout();
				cl.show(switchablePanel, "ORDER_VIEW");
				CardLayout cl2 = (CardLayout) filterPanel.getLayout();
				cl2.show(filterPanel, "ORDER_FILTER");
			}
		});
		btnOrders.setToolTipText("View pending orders");
		btnOrders.setForeground(Color.WHITE);
		btnOrders.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnOrders.setBackground(Color.BLACK);
		switcherButtons.add(btnOrders);
		
		filterPanel = new JPanel();
		filterPanel.setBackground(Color.GRAY);
		switcherPanel.add(filterPanel);
		filterPanel.setLayout(new CardLayout(0, 0));
		
		menuFilters = new JPanel();
		menuFilters.setBackground(Color.GRAY);
		filterPanel.add(menuFilters, "MENU_FILTER");
		menuFilters.setLayout(new GridLayout(5, 2, 50, 50));
		
		btnDesserts = new JButton("DESSERTS");
		btnDesserts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateMenu(DESSERT);
			}
		});
		
		btnAllItems = new JButton("ALL ITEMS");
		btnAllItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateMenu();
			}
		});
		btnAllItems.setToolTipText("See all items in the menu");
		btnAllItems.setForeground(Color.WHITE);
		btnAllItems.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnAllItems.setBackground(Color.BLACK);
		menuFilters.add(btnAllItems);
		
		btnMeals = new JButton("MEALS");
		btnMeals.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateMenu(MEAL);
			}
		});
		btnMeals.setToolTipText("See all meals in the menu");
		btnMeals.setForeground(Color.WHITE);
		btnMeals.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 22));
		btnMeals.setBackground(Color.BLACK);
		menuFilters.add(btnMeals);
		
		btnBeverages = new JButton("BEVERAGES");
		btnBeverages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateMenu(BEVERAGE);
			}
		});
		btnBeverages.setToolTipText("See all beverages in the menu");
		btnBeverages.setForeground(Color.WHITE);
		btnBeverages.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnBeverages.setBackground(Color.BLACK);
		menuFilters.add(btnBeverages);
		btnDesserts.setToolTipText("See all desserts in the menu");
		btnDesserts.setForeground(Color.WHITE);
		btnDesserts.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 22));
		btnDesserts.setBackground(Color.BLACK);
		menuFilters.add(btnDesserts);
		
		orderFilters = new JPanel();
		orderFilters.setBackground(Color.GRAY);
		filterPanel.add(orderFilters, "ORDER_FILTER");
		orderFilters.setLayout(new GridLayout(6, 2, 50, 50));
		
		btnAllOrders = new JButton("ALL ORDERS");
		btnAllOrders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateOrders();
			}
		});
		btnAllOrders.setToolTipText("View all non-completed orders");
		btnAllOrders.setForeground(Color.WHITE);
		btnAllOrders.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 22));
		btnAllOrders.setBackground(Color.BLACK);
		orderFilters.add(btnAllOrders);
		
		btnWaiting = new JButton("WAITING\r\n");
		btnWaiting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateOrders(STATUS_WAITING);
			}
		});
		btnWaiting.setToolTipText("View all orders with \"waiting\" status");
		btnWaiting.setForeground(Color.WHITE);
		btnWaiting.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnWaiting.setBackground(Color.BLACK);
		orderFilters.add(btnWaiting);
		
		btnProcessing = new JButton("PROCESSING");
		btnProcessing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateOrders(STATUS_PROCESSING);
			}
		});
		btnProcessing.setToolTipText("View all orders with \"processing\" status");
		btnProcessing.setForeground(Color.WHITE);
		btnProcessing.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 22));
		btnProcessing.setBackground(Color.BLACK);
		orderFilters.add(btnProcessing);
		
		btnServing = new JButton("SERVING");
		btnServing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateOrders(STATUS_SERVING);
			}
		});
		btnServing.setToolTipText("View all orders with \"serving\" status");
		btnServing.setForeground(Color.WHITE);
		btnServing.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnServing.setBackground(Color.BLACK);
		orderFilters.add(btnServing);
		
		btnCompleted = new JButton("COMPLETED");
		btnCompleted.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateOrders(STATUS_COMPLETED);
			}
		});
		btnCompleted.setToolTipText("View all orders with \"completed\" status");
		btnCompleted.setForeground(Color.WHITE);
		btnCompleted.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnCompleted.setBackground(Color.BLACK);
		orderFilters.add(btnCompleted);
		
		switchablePanel = new JPanel();
		contentPane.add(switchablePanel, BorderLayout.CENTER);
		switchablePanel.setLayout(new CardLayout(0, 0));
		
		menuViewPanel = new JPanel();
		menuViewPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.GRAY, Color.LIGHT_GRAY));
		menuViewPanel.setBackground(Color.LIGHT_GRAY);
		switchablePanel.add(menuViewPanel, "MENU_VIEW");
		menuViewPanel.setLayout(new BorderLayout(0, 10));
		
		menuNorthSpacer = new JPanel();
		menuNorthSpacer.setBackground(Color.LIGHT_GRAY);
		menuViewPanel.add(menuNorthSpacer, BorderLayout.NORTH);
		
		menuWestSpacer = new JPanel();
		menuWestSpacer.setBackground(Color.LIGHT_GRAY);
		menuViewPanel.add(menuWestSpacer, BorderLayout.WEST);
		
		menuEastSpacer = new JPanel();
		menuViewPanel.add(menuEastSpacer, BorderLayout.EAST);
		
		menuBtnsPanel = new JPanel();
		menuBtnsPanel.setBackground(Color.LIGHT_GRAY);
		menuViewPanel.add(menuBtnsPanel, BorderLayout.SOUTH);
		menuBtnsPanel.setLayout(new GridLayout(0, 3, 50, 0));
		
		btnAddItem = new JButton("ADD ITEM");
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//ServerFunctions.addItem(frame);
				new MenuAddDialog(Server.this);
				updateMenu();
			}
		});
		btnAddItem.setToolTipText("Add an item to the menu");
		btnAddItem.setForeground(Color.WHITE);
		btnAddItem.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnAddItem.setBackground(Color.BLACK);
		menuBtnsPanel.add(btnAddItem);
		
		btnRemoveItem = new JButton("REMOVE ITEM");
		btnRemoveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ServerFunctions.removeItem(frame);
				updateMenu();
			}
		});
		btnRemoveItem.setToolTipText("Remove an item from the menu");
		btnRemoveItem.setForeground(Color.WHITE);
		btnRemoveItem.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnRemoveItem.setBackground(Color.BLACK);
		menuBtnsPanel.add(btnRemoveItem);
		
		btnEditItem = new JButton("EDIT ITEM");
		btnEditItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ServerFunctions.editItem(frame);
				updateMenu();
			}
		});
		btnEditItem.setToolTipText("Edit an existing item in the menu (can't change category)");
		btnEditItem.setForeground(Color.WHITE);
		btnEditItem.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnEditItem.setBackground(Color.BLACK);
		menuBtnsPanel.add(btnEditItem);
		
		menuCenterPanel = new JPanel();
		menuCenterPanel.setBackground(Color.LIGHT_GRAY);
		menuCenterPanel.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
		menuCenterPanel.setLayout(new BorderLayout(0, 0));
		menuViewPanel.add(menuCenterPanel);
		
		menuPanel = new JPanel();
		menuPanel.setBackground(Color.DARK_GRAY);
		menuPanel.setLayout(new GridBagLayout());
		
		menuScrollPane = new JScrollPane();
		menuScrollPane.setViewportView(menuPanel);
		menuCenterPanel.add(menuScrollPane, BorderLayout.CENTER);
		
		menuColumnHeader = new JPanel();
		menuCenterPanel.add(menuColumnHeader, BorderLayout.NORTH);
		GridBagLayout gbl_menuColumnHeader = new GridBagLayout();
		gbl_menuColumnHeader.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_menuColumnHeader.rowHeights = new int[]{0, 0};
		gbl_menuColumnHeader.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_menuColumnHeader.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		menuColumnHeader.setLayout(gbl_menuColumnHeader);
		
		lblCategory = new JLabel("Category");
		lblCategory.setToolTipText("Meal, beverage or dessert");
		GridBagConstraints gbc_lblCategory = new GridBagConstraints();
		gbc_lblCategory.weightx = 0.25;
		gbc_lblCategory.ipady = 5;
		gbc_lblCategory.ipadx = 5;
		gbc_lblCategory.anchor = GridBagConstraints.WEST;
		gbc_lblCategory.insets = new Insets(0, 0, 0, 5);
		gbc_lblCategory.gridx = 0;
		gbc_lblCategory.gridy = 0;
		menuColumnHeader.add(lblCategory, gbc_lblCategory);
		
		lblItemID = new JLabel("Item ID");
		lblItemID.setToolTipText("You need this Item ID to add or remove an item in your order");
		GridBagConstraints gbc_lblItemID = new GridBagConstraints();
		gbc_lblItemID.weightx = 0.25;
		gbc_lblItemID.ipady = 5;
		gbc_lblItemID.ipadx = 5;
		gbc_lblItemID.anchor = GridBagConstraints.WEST;
		gbc_lblItemID.insets = new Insets(0, 0, 0, 5);
		gbc_lblItemID.gridx = 1;
		gbc_lblItemID.gridy = 0;
		menuColumnHeader.add(lblItemID, gbc_lblItemID);
		
		lblName = new JLabel("Item Name");
		lblName.setToolTipText("Name of the item");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.weightx = 0.5;
		gbc_lblName.ipady = 5;
		gbc_lblName.ipadx = 5;
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.insets = new Insets(0, 0, 0, 5);
		gbc_lblName.gridx = 2;
		gbc_lblName.gridy = 0;
		menuColumnHeader.add(lblName, gbc_lblName);
		
		lblPrice = new JLabel("Item Price");
		lblPrice.setToolTipText("The price of the item");
		GridBagConstraints gbc_lblPrice = new GridBagConstraints();
		gbc_lblPrice.weightx = 0.25;
		gbc_lblPrice.anchor = GridBagConstraints.WEST;
		gbc_lblPrice.gridx = 3;
		gbc_lblPrice.gridy = 0;
		menuColumnHeader.add(lblPrice, gbc_lblPrice);
		
		ordersViewPanel = new JPanel();
		ordersViewPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.GRAY, Color.LIGHT_GRAY));
		ordersViewPanel.setBackground(Color.LIGHT_GRAY);
		switchablePanel.add(ordersViewPanel, "ORDER_VIEW");
		ordersViewPanel.setLayout(new BorderLayout(0, 10));
		
		orderNorthSpacer = new JPanel();
		orderNorthSpacer.setBackground(Color.LIGHT_GRAY);
		ordersViewPanel.add(orderNorthSpacer, BorderLayout.NORTH);
		
		orderWestSpacer = new JPanel();
		orderWestSpacer.setBackground(Color.LIGHT_GRAY);
		ordersViewPanel.add(orderWestSpacer, BorderLayout.WEST);
		
		orderEastSpacer = new JPanel();
		ordersViewPanel.add(orderEastSpacer, BorderLayout.EAST);
		
		orderBtnsPanel = new JPanel();
		orderBtnsPanel.setBackground(Color.LIGHT_GRAY);
		ordersViewPanel.add(orderBtnsPanel, BorderLayout.SOUTH);
		orderBtnsPanel.setLayout(new GridLayout(0, 4, 50, 0));
		
		btnRefresh = new JButton("REFRESH");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateOrders();
			}
		});
		btnRefresh.setToolTipText("Refresh the list of orders");
		btnRefresh.setForeground(Color.WHITE);
		btnRefresh.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnRefresh.setBackground(Color.BLACK);
		orderBtnsPanel.add(btnRefresh);
		
		btnProcess = new JButton("PROCESS");
		btnProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ServerFunctions.processOrder(frame);
				updateOrders();
			}
		});
		btnProcess.setToolTipText("Change an order with status \"waiting\" to \"processing\"");
		btnProcess.setForeground(Color.WHITE);
		btnProcess.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnProcess.setBackground(Color.BLACK);
		orderBtnsPanel.add(btnProcess);
		
		btnServe = new JButton("SERVE");
		btnServe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ServerFunctions.serveOrder(frame);
				updateOrders();
			}
		});
		btnServe.setToolTipText("Change an order with status \"processing\" to \"serving\"");
		btnServe.setForeground(Color.WHITE);
		btnServe.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnServe.setBackground(Color.BLACK);
		orderBtnsPanel.add(btnServe);
		
		btnComplete = new JButton("COMPLETE");
		btnComplete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ServerFunctions.completeOrder(frame);
				updateOrders();
			}
		});
		btnComplete.setToolTipText("Change an order with status \"serving\" to \"completed\"");
		btnComplete.setForeground(Color.WHITE);
		btnComplete.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 24));
		btnComplete.setBackground(Color.BLACK);
		orderBtnsPanel.add(btnComplete);
		
		orderCenterPanel = new JPanel();
		orderCenterPanel.setBackground(Color.LIGHT_GRAY);
		orderCenterPanel.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
		ordersViewPanel.add(orderCenterPanel, BorderLayout.CENTER);
		orderCenterPanel.setLayout(new BorderLayout(0, 0));
		
		orderColumnHeader = new JPanel();
		orderCenterPanel.add(orderColumnHeader, BorderLayout.NORTH);
		GridBagLayout gbl_orderColumnHeader = new GridBagLayout();
		gbl_orderColumnHeader.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_orderColumnHeader.rowHeights = new int[]{0, 0};
		gbl_orderColumnHeader.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_orderColumnHeader.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		orderColumnHeader.setLayout(gbl_orderColumnHeader);
		
		lblOrderNo = new JLabel("Order No.");
		lblOrderNo.setToolTipText("The number of an order");
		GridBagConstraints gbc_lblOrderNo = new GridBagConstraints();
		gbc_lblOrderNo.weightx = 0.1;
		gbc_lblOrderNo.ipady = 5;
		gbc_lblOrderNo.ipadx = 5;
		gbc_lblOrderNo.anchor = GridBagConstraints.WEST;
		gbc_lblOrderNo.insets = new Insets(0, 0, 0, 5);
		gbc_lblOrderNo.gridx = 0;
		gbc_lblOrderNo.gridy = 0;
		orderColumnHeader.add(lblOrderNo, gbc_lblOrderNo);
		
		lblOrderContents = new JLabel("Order Contents");
		lblOrderContents.setToolTipText("Contents of the order.");
		GridBagConstraints gbc_lblOrderContents = new GridBagConstraints();
		gbc_lblOrderContents.weightx = 0.5;
		gbc_lblOrderContents.ipady = 5;
		gbc_lblOrderContents.ipadx = 5;
		gbc_lblOrderContents.anchor = GridBagConstraints.WEST;
		gbc_lblOrderContents.insets = new Insets(0, 0, 0, 5);
		gbc_lblOrderContents.gridx = 1;
		gbc_lblOrderContents.gridy = 0;
		orderColumnHeader.add(lblOrderContents, gbc_lblOrderContents);
		
		lblOrderStatus = new JLabel("Order Status");
		lblOrderStatus.setToolTipText("Name of the item");
		GridBagConstraints gbc_lblOrderStatus = new GridBagConstraints();
		gbc_lblOrderStatus.weightx = 0.15;
		gbc_lblOrderStatus.ipady = 5;
		gbc_lblOrderStatus.ipadx = 5;
		gbc_lblOrderStatus.anchor = GridBagConstraints.WEST;
		gbc_lblOrderStatus.insets = new Insets(0, 0, 0, 5);
		gbc_lblOrderStatus.gridx = 2;
		gbc_lblOrderStatus.gridy = 0;
		orderColumnHeader.add(lblOrderStatus, gbc_lblOrderStatus);
		
		lblTotal = new JLabel("Order Total");
		lblTotal.setToolTipText("The price of the item");
		GridBagConstraints gbc_lblTotal = new GridBagConstraints();
		gbc_lblTotal.weightx = 0.15;
		gbc_lblTotal.anchor = GridBagConstraints.WEST;
		gbc_lblTotal.gridx = 3;
		gbc_lblTotal.gridy = 0;
		orderColumnHeader.add(lblTotal, gbc_lblTotal);
		
		orderPanel = new JPanel();
		orderPanel.setBackground(Color.DARK_GRAY);
		orderPanel.setLayout(new GridBagLayout());
		
		orderScrollPane = new JScrollPane();
		orderScrollPane.setViewportView(orderPanel);
		orderCenterPanel.add(orderScrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * Method that trie to get the local IP address
	 * in LAN connection
	 */
	@SuppressWarnings("rawtypes")
	public static void getLocalIP() throws SocketException {
		
		String ip;

		Enumeration e = NetworkInterface.getNetworkInterfaces();
		while(e.hasMoreElements())
		{
  	  		NetworkInterface n = (NetworkInterface) e.nextElement();
    		Enumeration ee = n.getInetAddresses();
    		while (ee.hasMoreElements())
    		{
    			ip = ((InetAddress) ee.nextElement()).getHostAddress();
    			if(ip.contains("192.168.")) {
    				host = ip;
    				break;
    			}
    		}
		}
		JOptionPane.showMessageDialog(frame, "This server's address is " + host, "Server Address", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Updates the content of menu without any Filter
	 * (displays all)
	 */
	static void updateMenu() {
		//clear contents of the menu before updating
		frame.menuPanel.removeAll();
				
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.ipadx = 10;
		gbc.ipady = 10;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH; 
				
		for(Item item: menu.values()) {
			frame.menuPanel.add(new ServerItemPanel(item), gbc);
			gbc.gridy++;
		}

		frame.revalidate();
		frame.repaint();
	}
	/**
	 * filter constants
	 */
	static final public int MEAL = 0;
	static final public int BEVERAGE = 1;
	static final public int DESSERT = 2;
	
	/***
	 * Updates content of Menu with filtering
	 *  - pass 0 for meals
	 *  - pass 1 for beverages
	 *  - pass 2 for desserts	
	 */
	static void updateMenu(int filter) {
		//clear contents of the menu before updating
		frame.menuPanel.removeAll();
				
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.ipadx = 10;
		gbc.ipady = 10;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
				
		switch(filter) {
		case MEAL:
			for(Item item: menu.values()) {
				if(item.getCategory().equalsIgnoreCase("MEAL")) {
					frame.menuPanel.add(new ServerItemPanel(item), gbc);
					gbc.gridy++;
				}
			}
			break;
		case BEVERAGE:
			for(Item item: menu.values()) {
				if(item.getCategory().equalsIgnoreCase("BEVERAGE")) {
					frame.menuPanel.add(new ServerItemPanel(item), gbc);
					gbc.gridy++;
				}
			}
			break;
		case DESSERT:
			for(Item item: menu.values()) {
				if(item.getCategory().equalsIgnoreCase("DESSERT")) {
					frame.menuPanel.add(new ServerItemPanel(item), gbc);
					gbc.gridy++;
				}
			}
			break;
		default:
			updateMenu();
		}

		frame.revalidate();
		frame.repaint();
	}
	
	/***
	 * Update the displayed orders in the interface
	 */
	static void updateOrders() {
		//clear contents of the order before updating
		frame.orderPanel.removeAll();
				
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.ipadx = 10;
		gbc.ipady = 10;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH; 
				
		for(Order order: orders.values()) {
			if(!order.getStatus().equalsIgnoreCase("COMPLETED")) {
				frame.orderPanel.add(new OrderPanel(order), gbc);
				gbc.gridy++;
			}
		}

		frame.revalidate();
		frame.repaint();
	}
	
	/**
	 * List of valid arguments for refreshOrder(int filter)
	 */
	static final int STATUS_WAITING = 0;
	static final int STATUS_PROCESSING = 1;
	static final int STATUS_SERVING = 2;
	static final int STATUS_COMPLETED = 3;
	
	/**
	 * updates GUI with filtered orders according to their status
	 * valid arguments to pass are the ones above
	 */
	public static void updateOrders(int filter) {
		
		frame.orderPanel.removeAll();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.ipadx = 10;
		gbc.ipady = 10;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH; 
		
		for(Order order: Server.orders.values()) {
			switch(filter) {
			case STATUS_WAITING:
				if(order.getStatus().equalsIgnoreCase("WAITING")) {
					frame.orderPanel.add(new OrderPanel(order), gbc);
					gbc.gridy++;
				}	
				break;
			case STATUS_PROCESSING:
				if(order.getStatus().equalsIgnoreCase("PROCESSING")) {
					frame.orderPanel.add(new OrderPanel(order), gbc);
					gbc.gridy++;
				}	
				break;
			case STATUS_SERVING:
				if(order.getStatus().equalsIgnoreCase("SERVING")) {
					frame.orderPanel.add(new OrderPanel(order), gbc);
					gbc.gridy++;
				}	
				break;
			case STATUS_COMPLETED:
				if(order.getStatus().equalsIgnoreCase("COMPLETED")) {
					frame.orderPanel.add(new OrderPanel(order), gbc);
					gbc.gridy++;
				}	
				break;
			default:
				updateOrders();
			}
		}
		frame.revalidate();
		frame.repaint();
	}
}
