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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.JSeparator;
import javax.swing.border.EtchedBorder;
import shared_classes.Order;
import java.awt.GridBagLayout;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 * Dialog that shows up if we want to change the status of an order
 * Asks for Item name, price, and category
 */
public class OrderProceedDialog extends JDialog {


	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JFrame headComponent = null;
	private JPanel buttonPanel, detailsPanel;
	private JButton btnAdd, btnCancel;
	private JSeparator separator, separator_1, separator_2, separator_3;
	private JLabel lblHeader, lblOrderNo, lblName, lblTotal, lblContents, lblStatus, lblContentList;
	private JTextField txtfldOrderNo, txtfldName, txtfldTotal, txtfldStatus;
	private Order order;

	/**
	 * Create the dialog.
	 */
	public OrderProceedDialog(JFrame headComponent, int orderNum) throws IllegalArgumentException{
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
		order = Server.orders.get(orderNum);
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
		
		String btnlbl;
		String status = order.getStatus().toUpperCase();
		switch(status) {
		case "WAITING":
			btnlbl = "Process";
			break;
		case "PROCESSING":
			btnlbl = "Serve";
			break;
		case "SERVING":
			btnlbl = "Complete";
			break;
		default:
			btnlbl = "Okay";
			break;
		}
		btnAdd = new JButton(btnlbl);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!btnlbl.equalsIgnoreCase("Okay")) {
					Server.at.sendToAnnouncer(headComponent, btnlbl.toUpperCase(), txtfldOrderNo.getText());
					Server.updateOrders();
				}
				dispose();
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
		gbl_detailsPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_detailsPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_detailsPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_detailsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		detailsPanel.setLayout(gbl_detailsPanel);
		
		lblOrderNo = new JLabel("Order No :");
		lblOrderNo.setHorizontalAlignment(SwingConstants.CENTER);
		lblOrderNo.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 20));
		GridBagConstraints gbc_lblOrderNo = new GridBagConstraints();
		gbc_lblOrderNo.anchor = GridBagConstraints.WEST;
		gbc_lblOrderNo.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrderNo.gridx = 2;
		gbc_lblOrderNo.gridy = 3;
		detailsPanel.add(lblOrderNo, gbc_lblOrderNo);
		
		txtfldOrderNo = new JTextField(order.getOrderNo()+"");
		txtfldOrderNo.setFont(new Font("Verdana", Font.PLAIN, 12));
		txtfldOrderNo.setEditable(false);
		GridBagConstraints gbc_txtfldOrderNo = new GridBagConstraints();
		gbc_txtfldOrderNo.gridwidth = 8;
		gbc_txtfldOrderNo.insets = new Insets(0, 0, 5, 5);
		gbc_txtfldOrderNo.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtfldOrderNo.gridx = 4;
		gbc_txtfldOrderNo.gridy = 3;
		detailsPanel.add(txtfldOrderNo, gbc_txtfldOrderNo);
		txtfldOrderNo.setColumns(10);
		
		lblName = new JLabel("Name:");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 20));
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.gridx = 2;
		gbc_lblName.gridy = 4;
		detailsPanel.add(lblName, gbc_lblName);
		
		txtfldName = new JTextField(order.getName());
		txtfldName.setEditable(false);
		txtfldName.setFont(new Font("Verdana", Font.PLAIN, 12));
		txtfldName.setColumns(10);
		GridBagConstraints gbc_txtfldName = new GridBagConstraints();
		gbc_txtfldName.gridwidth = 8;
		gbc_txtfldName.insets = new Insets(0, 0, 5, 5);
		gbc_txtfldName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtfldName.gridx = 4;
		gbc_txtfldName.gridy = 4;
		detailsPanel.add(txtfldName, gbc_txtfldName);
		
		separator_2 = new JSeparator();
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.insets = new Insets(0, 0, 5, 0);
		gbc_separator_2.gridx = 13;
		gbc_separator_2.gridy = 5;
		detailsPanel.add(separator_2, gbc_separator_2);
		
		lblStatus = new JLabel("Status:");
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 20));
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.anchor = GridBagConstraints.WEST;
		gbc_lblStatus.insets = new Insets(0, 0, 5, 5);
		gbc_lblStatus.gridx = 2;
		gbc_lblStatus.gridy = 6;
		detailsPanel.add(lblStatus, gbc_lblStatus);
		
		txtfldStatus = new JTextField(order.getStatus());
		txtfldStatus.setEditable(false);
		txtfldStatus.setFont(new Font("Verdana", Font.PLAIN, 12));
		txtfldStatus.setColumns(10);
		GridBagConstraints gbc_txtfldStatus = new GridBagConstraints();
		gbc_txtfldStatus.gridwidth = 8;
		gbc_txtfldStatus.insets = new Insets(0, 0, 5, 5);
		gbc_txtfldStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtfldStatus.gridx = 4;
		gbc_txtfldStatus.gridy = 6;
		detailsPanel.add(txtfldStatus, gbc_txtfldStatus);
		
		lblTotal = new JLabel("Total:");
		lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotal.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 20));
		GridBagConstraints gbc_lblTotal = new GridBagConstraints();
		gbc_lblTotal.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotal.anchor = GridBagConstraints.WEST;
		gbc_lblTotal.gridx = 2;
		gbc_lblTotal.gridy = 7;
		detailsPanel.add(lblTotal, gbc_lblTotal);
		
		txtfldTotal = new JTextField("P" + order.getTotal());
		txtfldTotal.setEditable(false);
		txtfldTotal.setFont(new Font("Verdana", Font.PLAIN, 12));
		txtfldTotal.setColumns(10);
		GridBagConstraints gbc_txtfldTotal = new GridBagConstraints();
		gbc_txtfldTotal.gridwidth = 8;
		gbc_txtfldTotal.insets = new Insets(0, 0, 5, 5);
		gbc_txtfldTotal.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtfldTotal.gridx = 4;
		gbc_txtfldTotal.gridy = 7;
		detailsPanel.add(txtfldTotal, gbc_txtfldTotal);
		
		separator_3 = new JSeparator();
		GridBagConstraints gbc_separator_3 = new GridBagConstraints();
		gbc_separator_3.insets = new Insets(0, 0, 5, 5);
		gbc_separator_3.gridx = 1;
		gbc_separator_3.gridy = 8;
		detailsPanel.add(separator_3, gbc_separator_3);
		
		lblContents = new JLabel("Contents:");
		lblContents.setHorizontalAlignment(SwingConstants.CENTER);
		lblContents.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 20));
		GridBagConstraints gbc_lblContents = new GridBagConstraints();
		gbc_lblContents.anchor = GridBagConstraints.WEST;
		gbc_lblContents.insets = new Insets(0, 0, 5, 5);
		gbc_lblContents.gridx = 2;
		gbc_lblContents.gridy = 9;
		detailsPanel.add(lblContents, gbc_lblContents);
		
		String html = "<html><body width=200px>" + order.getContents() + "</body></html>";
		lblContentList = new JLabel(html);
		lblContentList.setFont(new Font("Verdana", Font.PLAIN, 12));
		GridBagConstraints gbc_lblContentList = new GridBagConstraints();
		gbc_lblContentList.anchor = GridBagConstraints.WEST;
		gbc_lblContentList.gridwidth = 8;
		gbc_lblContentList.insets = new Insets(0, 0, 5, 5);
		gbc_lblContentList.gridx = 4;
		gbc_lblContentList.gridy = 9;
		detailsPanel.add(lblContentList, gbc_lblContentList);
		
		lblHeader = new JLabel("Order Details");
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

