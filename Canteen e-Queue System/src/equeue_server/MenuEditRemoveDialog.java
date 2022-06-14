package equeue_server;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.JSeparator;
import javax.swing.border.EtchedBorder;
import shared_classes.Item;
import java.awt.GridBagLayout;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 * Dialog that shows up if we want to edit an item from the menu
 * Asks for new item name and new price
 */
public class MenuEditRemoveDialog extends JDialog {


	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JFrame headComponent = null;
	private ImageIcon icon;
	private JPanel buttonPanel, detailsPanel;
	private JButton btnEditItem, btnCancel, btnRemove;
	private JSeparator separator_1, separator_2, separator_3;
	private JLabel lblHeader, lblItemID, lblName, lblPrice, lblcategory, lbIcon;
	private JTextField txtfldID, txtfldName, txtfldPrice;

	/**
	 * Create the dialog.
	 */
	public MenuEditRemoveDialog(JFrame headComponent, Item item) throws IllegalArgumentException{
		super(headComponent, ModalityType.APPLICATION_MODAL);
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// nothing, just go on with default LAF
		}
		if(headComponent!=null) {
			this.headComponent = headComponent;
		}
		if(item==null) {
			throw new IllegalArgumentException("You need to pass an item to this constructor!");
		}
		if(item.getCategory().equalsIgnoreCase("BEVERAGE")) {
			icon = new ImageIcon(new ImageIcon(MenuEditRemoveDialog.class.getResource("/shared_classes/Beverage.png")).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
		}else if(item.getCategory().equalsIgnoreCase("DESSERT")) {
			icon = new ImageIcon(new ImageIcon(MenuEditRemoveDialog.class.getResource("/shared_classes/Dessert.png")).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
		}else {
			icon = new ImageIcon(new ImageIcon(MenuEditRemoveDialog.class.getResource("/shared_classes/Meal.png")).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
		}
		setUndecorated(true);
		setSize(450, 300);
		setLocationRelativeTo(headComponent);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.GRAY);
		contentPanel.setBorder(new LineBorder(Color.DARK_GRAY, 4, true));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 5));
		
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.GRAY);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new GridLayout(0, 4, 5, 5));
		
		separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setForeground(Color.GRAY);
		separator_1.setBackground(Color.GRAY);
		buttonPanel.add(separator_1);
		
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ServerFunctions.removeItem(headComponent, txtfldID.getText());
				Server.updateMenu();
			}
		});
		btnRemove.setForeground(Color.WHITE);
		btnRemove.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 18));
		btnRemove.setBackground(Color.BLACK);
		buttonPanel.add(btnRemove);
		
		btnEditItem = new JButton("Edit");
		btnEditItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txtfldName.getText().isBlank()) {
					JOptionPane.showMessageDialog(headComponent, "Name can't be empty!", "Failed", JOptionPane.ERROR_MESSAGE);
				}else if(txtfldPrice.getText().isBlank()) {
					JOptionPane.showMessageDialog(headComponent, "Price can't be empty!", "Failed", JOptionPane.ERROR_MESSAGE);
				}else {
					try {
						ServerFunctions.editItem(headComponent, txtfldID.getText(), txtfldName.getText(), Double.parseDouble(txtfldPrice.getText()));
						dispose();
						Server.updateMenu();
					}catch(NumberFormatException ex) {
						JOptionPane.showMessageDialog(headComponent, "Invalid price!", "Failed", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnEditItem.setForeground(Color.WHITE);
		btnEditItem.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 18));
		btnEditItem.setBackground(Color.BLACK);
		buttonPanel.add(btnEditItem);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setForeground(Color.WHITE);
		btnCancel.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 18));
		btnCancel.setBackground(Color.BLACK);
		buttonPanel.add(btnCancel);
		
		detailsPanel = new JPanel();
		detailsPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.LIGHT_GRAY, null));
		detailsPanel.setBackground(Color.LIGHT_GRAY);
		contentPanel.add(detailsPanel, BorderLayout.CENTER);
		GridBagLayout gbl_detailsPanel = new GridBagLayout();
		gbl_detailsPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_detailsPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_detailsPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_detailsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		detailsPanel.setLayout(gbl_detailsPanel);
		
		lbIcon = new JLabel("");
		lbIcon.setHorizontalAlignment(SwingConstants.LEFT);
		lbIcon.setIcon(icon);
		GridBagConstraints gbc_lbIcon = new GridBagConstraints();
		gbc_lbIcon.gridwidth = 2;
		gbc_lbIcon.insets = new Insets(0, 0, 5, 5);
		gbc_lbIcon.gridx = 2;
		gbc_lbIcon.gridy = 1;
		detailsPanel.add(lbIcon, gbc_lbIcon);
		
		lblcategory = new JLabel("(category)");
		lblcategory.setText(item.getCategory());
		lblcategory.setHorizontalAlignment(SwingConstants.CENTER);
		lblcategory.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 22));
		GridBagConstraints gbc_lblcategory = new GridBagConstraints();
		gbc_lblcategory.insets = new Insets(0, 0, 5, 5);
		gbc_lblcategory.gridx = 4;
		gbc_lblcategory.gridy = 1;
		detailsPanel.add(lblcategory, gbc_lblcategory);
		
		lblItemID = new JLabel("Item ID:");
		lblItemID.setHorizontalAlignment(SwingConstants.CENTER);
		lblItemID.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 20));
		GridBagConstraints gbc_lblItemID = new GridBagConstraints();
		gbc_lblItemID.anchor = GridBagConstraints.WEST;
		gbc_lblItemID.insets = new Insets(0, 0, 5, 5);
		gbc_lblItemID.gridx = 2;
		gbc_lblItemID.gridy = 3;
		detailsPanel.add(lblItemID, gbc_lblItemID);
		
		txtfldID = new JTextField();
		txtfldID.setEditable(false);
		txtfldID.setText(item.getItemID());
		txtfldID.setEnabled(false);
		GridBagConstraints gbc_txtfldID = new GridBagConstraints();
		gbc_txtfldID.gridwidth = 6;
		gbc_txtfldID.insets = new Insets(0, 0, 5, 5);
		gbc_txtfldID.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtfldID.gridx = 4;
		gbc_txtfldID.gridy = 3;
		detailsPanel.add(txtfldID, gbc_txtfldID);
		txtfldID.setColumns(10);
		
		separator_2 = new JSeparator();
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.insets = new Insets(0, 0, 5, 0);
		gbc_separator_2.gridx = 11;
		gbc_separator_2.gridy = 3;
		detailsPanel.add(separator_2, gbc_separator_2);
		
		separator_3 = new JSeparator();
		GridBagConstraints gbc_separator_3 = new GridBagConstraints();
		gbc_separator_3.insets = new Insets(0, 0, 5, 5);
		gbc_separator_3.gridx = 1;
		gbc_separator_3.gridy = 4;
		detailsPanel.add(separator_3, gbc_separator_3);
		
		lblName = new JLabel("Name:");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 20));
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.gridx = 2;
		gbc_lblName.gridy = 4;
		detailsPanel.add(lblName, gbc_lblName);
		
		txtfldName = new JTextField();
		txtfldName.setText(item.getName());
		txtfldName.setColumns(10);
		GridBagConstraints gbc_txtfldName = new GridBagConstraints();
		gbc_txtfldName.gridwidth = 6;
		gbc_txtfldName.insets = new Insets(0, 0, 5, 5);
		gbc_txtfldName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtfldName.gridx = 4;
		gbc_txtfldName.gridy = 4;
		detailsPanel.add(txtfldName, gbc_txtfldName);
		
		lblPrice = new JLabel("Price:");
		lblPrice.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrice.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 20));
		GridBagConstraints gbc_lblPrice = new GridBagConstraints();
		gbc_lblPrice.insets = new Insets(0, 0, 5, 5);
		gbc_lblPrice.anchor = GridBagConstraints.WEST;
		gbc_lblPrice.gridx = 2;
		gbc_lblPrice.gridy = 5;
		detailsPanel.add(lblPrice, gbc_lblPrice);
		
		txtfldPrice = new JTextField();
		txtfldPrice.setText(item.getPrice()+"");
		txtfldPrice.setColumns(10);
		GridBagConstraints gbc_txtfldPrice = new GridBagConstraints();
		gbc_txtfldPrice.gridwidth = 6;
		gbc_txtfldPrice.insets = new Insets(0, 0, 5, 5);
		gbc_txtfldPrice.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtfldPrice.gridx = 4;
		gbc_txtfldPrice.gridy = 5;
		detailsPanel.add(txtfldPrice, gbc_txtfldPrice);
		
		lblHeader = new JLabel("Item Information");
		lblHeader.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 22));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblHeader, BorderLayout.NORTH);
		
		FrameDragListener frameDragListener = new FrameDragListener(this);
        addMouseListener(frameDragListener);
        addMouseMotionListener(frameDragListener);
        pack();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	@Override
	public void dispose() {
		if(headComponent!=null)
			headComponent.setEnabled(true);
		super.dispose();
	}

}

//make the dialog draggable
class FrameDragListener extends MouseAdapter {

    private final JDialog frame;
    private Point mouseDownCompCoords = null;

    public FrameDragListener(JDialog frame) {
        this.frame = frame;
    }

    public void mouseReleased(MouseEvent e) {
        mouseDownCompCoords = null;
    }

    public void mousePressed(MouseEvent e) {
        mouseDownCompCoords = e.getPoint();
    }

    public void mouseDragged(MouseEvent e) {
        Point currCoords = e.getLocationOnScreen();
        frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
    }
}
