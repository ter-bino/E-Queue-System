package equeue_client;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import shared_classes.Item;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.GridBagConstraints;
import javax.swing.JSpinner;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * A dialog that appears when we click on an item from the menu
 * The function is to add an item to an existing order within
 * the client app.
 */
public class OrderAddDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JFrame headComponent;
	private JPanel mainContent, buttonPanel;
	private JLabel lblItemInfo, lblToAdd, headerLabel;
	private JSpinner amountSpinner;
	private JButton btnCancel, btnAdd;

	/**
	 * Create the dialog.
	 */
	public OrderAddDialog(JFrame headComponent, Item item) {
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
		setSize(450, 300);
		setUndecorated(true);
		setLocationRelativeTo(headComponent);
		getContentPane().setLayout(new BorderLayout());
		
		contentPanel.setBackground(Color.GRAY);
		contentPanel.setBorder(new LineBorder(new Color(64, 64, 64), 4, true));
		contentPanel.setLayout(new BorderLayout(0, 0));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		mainContent = new JPanel();
		mainContent.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		mainContent.setBackground(Color.LIGHT_GRAY);
		contentPanel.add(mainContent, BorderLayout.CENTER);
		GridBagLayout gbl_mainContent = new GridBagLayout();
		gbl_mainContent.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_mainContent.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_mainContent.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_mainContent.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		mainContent.setLayout(gbl_mainContent);
		
		lblItemInfo = new JLabel(item.toString());
		lblItemInfo.setFont(new Font("Verdana Pro Cond Black", Font.BOLD | Font.ITALIC, 22));
		lblItemInfo.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblItemInfo = new GridBagConstraints();
		gbc_lblItemInfo.fill = GridBagConstraints.BOTH;
		gbc_lblItemInfo.gridwidth = 13;
		gbc_lblItemInfo.insets = new Insets(0, 0, 5, 5);
		gbc_lblItemInfo.gridx = 2;
		gbc_lblItemInfo.gridy = 1;
		mainContent.add(lblItemInfo, gbc_lblItemInfo);
		
		lblToAdd = new JLabel("Amount to add:");
		lblToAdd.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 18));
		GridBagConstraints gbc_lblToAdd = new GridBagConstraints();
		gbc_lblToAdd.anchor = GridBagConstraints.EAST;
		gbc_lblToAdd.gridwidth = 3;
		gbc_lblToAdd.insets = new Insets(0, 0, 0, 5);
		gbc_lblToAdd.gridx = 8;
		gbc_lblToAdd.gridy = 7;
		mainContent.add(lblToAdd, gbc_lblToAdd);
		
		amountSpinner = new JSpinner();
		amountSpinner.setFont(new Font("Verdana", Font.PLAIN, 18));
		GridBagConstraints gbc_amountSpinner = new GridBagConstraints();
		gbc_amountSpinner.insets = new Insets(0, 0, 0, 5);
		gbc_amountSpinner.fill = GridBagConstraints.BOTH;
		gbc_amountSpinner.anchor = GridBagConstraints.EAST;
		gbc_amountSpinner.gridwidth = 4;
		gbc_amountSpinner.gridx = 11;
		gbc_amountSpinner.gridy = 7;
		mainContent.add(amountSpinner, gbc_amountSpinner);
		
		headerLabel = new JLabel("ADD AN ITEM");
		headerLabel.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 22));
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(headerLabel, BorderLayout.NORTH);
		
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.GRAY);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client.order.add(Client.menu.get(item.getItemID()), (Integer)amountSpinner.getValue());
				dispose();
				JOptionPane.showMessageDialog(headComponent,  "Item added to order successfully", "Successful", JOptionPane.INFORMATION_MESSAGE);
			}
		});
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
