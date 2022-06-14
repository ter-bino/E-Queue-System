package equeue_server;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.JSeparator;
import javax.swing.border.EtchedBorder;
import java.awt.GridBagLayout;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerListModel;
import javax.swing.border.LineBorder;

/**
 * Dialog that shows up if we want to add an item to the menu
 * Asks for Item name, price, and category
 */
public class MenuAddDialog extends JDialog {


	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JFrame headComponent = null;
	private JPanel buttonPanel, detailsPanel;
	private JButton btnAdd, btnCancel;
	private JSeparator separator_1, separator_2, separator_3;
	private JLabel lblHeader, lblItemID, lblName, lblPrice;
	private JTextField txtfldID, txtfldName, txtfldPrice;
	private JLabel lblCategory;
	private JSpinner choiceSpinner;
	private JSeparator separator;

	/**
	 * Create the dialog.
	 */
	public MenuAddDialog(JFrame headComponent) {
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
		setUndecorated(true);
		setSize(452, 257);
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
		
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txtfldName.getText().isBlank()) {
					JOptionPane.showMessageDialog(headComponent, "Name can't be empty!", "Failed", JOptionPane.ERROR_MESSAGE);
				}else if(txtfldPrice.getText().isBlank()) {
					JOptionPane.showMessageDialog(headComponent, "Price can't be empty!", "Failed", JOptionPane.ERROR_MESSAGE);
				}else {
					try {
						byte type;
						switch(((String)choiceSpinner.getValue()).toUpperCase()) {
						case "MEAL":
							type = Server.MEAL;
							break;
						case "BEVERAGE":
							type = Server.BEVERAGE;
							break;
						default:
							type = Server.DESSERT;
						}
						ServerFunctions.addItem(headComponent, txtfldName.getText(), Double.parseDouble(txtfldPrice.getText()), type);
						dispose();
						Server.updateMenu();
					}catch(NumberFormatException ex) {
						JOptionPane.showMessageDialog(headComponent, "Invalid price!", "Failed", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.GRAY);
		separator.setBackground(Color.GRAY);
		buttonPanel.add(separator);
		btnAdd.setForeground(Color.WHITE);
		btnAdd.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 18));
		btnAdd.setBackground(Color.BLACK);
		buttonPanel.add(btnAdd);
		
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
		gbl_detailsPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_detailsPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_detailsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		detailsPanel.setLayout(gbl_detailsPanel);
		
		lblItemID = new JLabel("Item ID:");
		lblItemID.setHorizontalAlignment(SwingConstants.CENTER);
		lblItemID.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 20));
		GridBagConstraints gbc_lblItemID = new GridBagConstraints();
		gbc_lblItemID.anchor = GridBagConstraints.WEST;
		gbc_lblItemID.insets = new Insets(0, 0, 5, 5);
		gbc_lblItemID.gridx = 2;
		gbc_lblItemID.gridy = 4;
		detailsPanel.add(lblItemID, gbc_lblItemID);
		
		txtfldID = new JTextField();
		txtfldID.setText("(set by program)");
		txtfldID.setEnabled(false);
		txtfldID.setFont(new Font("Verdana", Font.PLAIN, 12));
		txtfldID.setEditable(false);
		GridBagConstraints gbc_txtfldID = new GridBagConstraints();
		gbc_txtfldID.gridheight = 2;
		gbc_txtfldID.gridwidth = 6;
		gbc_txtfldID.insets = new Insets(0, 0, 5, 5);
		gbc_txtfldID.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtfldID.gridx = 4;
		gbc_txtfldID.gridy = 4;
		detailsPanel.add(txtfldID, gbc_txtfldID);
		txtfldID.setColumns(10);
		
		separator_2 = new JSeparator();
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.insets = new Insets(0, 0, 5, 0);
		gbc_separator_2.gridx = 11;
		gbc_separator_2.gridy = 5;
		detailsPanel.add(separator_2, gbc_separator_2);
		
		lblName = new JLabel("Name:");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 20));
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.gridx = 2;
		gbc_lblName.gridy = 6;
		detailsPanel.add(lblName, gbc_lblName);
		
		txtfldName = new JTextField();
		txtfldName.setFont(new Font("Verdana", Font.PLAIN, 12));
		txtfldName.setColumns(10);
		GridBagConstraints gbc_txtfldName = new GridBagConstraints();
		gbc_txtfldName.gridwidth = 6;
		gbc_txtfldName.insets = new Insets(0, 0, 5, 5);
		gbc_txtfldName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtfldName.gridx = 4;
		gbc_txtfldName.gridy = 6;
		detailsPanel.add(txtfldName, gbc_txtfldName);
		
		lblPrice = new JLabel("Price:");
		lblPrice.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrice.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 20));
		GridBagConstraints gbc_lblPrice = new GridBagConstraints();
		gbc_lblPrice.insets = new Insets(0, 0, 5, 5);
		gbc_lblPrice.anchor = GridBagConstraints.WEST;
		gbc_lblPrice.gridx = 2;
		gbc_lblPrice.gridy = 7;
		detailsPanel.add(lblPrice, gbc_lblPrice);
		
		txtfldPrice = new JTextField();
		txtfldPrice.setFont(new Font("Verdana", Font.PLAIN, 12));
		txtfldPrice.setColumns(10);
		GridBagConstraints gbc_txtfldPrice = new GridBagConstraints();
		gbc_txtfldPrice.gridwidth = 6;
		gbc_txtfldPrice.insets = new Insets(0, 0, 5, 5);
		gbc_txtfldPrice.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtfldPrice.gridx = 4;
		gbc_txtfldPrice.gridy = 7;
		detailsPanel.add(txtfldPrice, gbc_txtfldPrice);
		
		separator_3 = new JSeparator();
		GridBagConstraints gbc_separator_3 = new GridBagConstraints();
		gbc_separator_3.insets = new Insets(0, 0, 5, 5);
		gbc_separator_3.gridx = 1;
		gbc_separator_3.gridy = 8;
		detailsPanel.add(separator_3, gbc_separator_3);
		
		lblCategory = new JLabel("Category:");
		lblCategory.setHorizontalAlignment(SwingConstants.CENTER);
		lblCategory.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 20));
		GridBagConstraints gbc_lblCategory = new GridBagConstraints();
		gbc_lblCategory.anchor = GridBagConstraints.WEST;
		gbc_lblCategory.insets = new Insets(0, 0, 5, 5);
		gbc_lblCategory.gridx = 2;
		gbc_lblCategory.gridy = 9;
		detailsPanel.add(lblCategory, gbc_lblCategory);
		
		choiceSpinner = new JSpinner();
		choiceSpinner.setFont(new Font("Verdana", Font.PLAIN, 12));
		choiceSpinner.setModel(new SpinnerListModel(new String[] {"Meal", "Beverage", "Dessert"}));
		GridBagConstraints gbc_choiceSpinner = new GridBagConstraints();
		gbc_choiceSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_choiceSpinner.gridwidth = 6;
		gbc_choiceSpinner.insets = new Insets(0, 0, 5, 5);
		gbc_choiceSpinner.gridx = 4;
		gbc_choiceSpinner.gridy = 9;
		((DefaultEditor)choiceSpinner.getEditor()).getTextField().setEditable(false);
		detailsPanel.add(choiceSpinner, gbc_choiceSpinner);
		
		lblHeader = new JLabel("Adding Item to Menu");
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

