package shared_classes;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import javax.swing.border.LineBorder;
import equeue_server.OrderProceedDialog;

/**
 * Each instance of this class is a panel
 * for a specific order in the displayed orders
 * in the Server app.
 */
public class OrderPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	//Font used in the whole GUI
	static Font font = new Font("Verdana Pro Cond Black", Font.BOLD, 32);
	
	//instance variables
	private ImageIcon icon;
	private JLabel category, orderNo, orderContents, orderTotal;
	

	/**
	 * Create the panel.
	 */
	public OrderPanel(Order order) {
		
		String html;	//helps in formatting
		
		setBorder(new LineBorder(Color.WHITE, 1, true));
		setBackground(Color.LIGHT_GRAY);
		setToolTipText(order.toString());
		setLayout(new GridBagLayout());
		
		//Category label
		html = "<html><body width='100px'>" + order.getOrderNo() + "</body></html>";
		category = new JLabel(html);
		category.setIcon(icon);
		category.setHorizontalAlignment(JLabel.LEFT);
		category.setFont(font);
		GridBagConstraints gbc_lblCategory = new GridBagConstraints();
		gbc_lblCategory.ipadx = 5;
		gbc_lblCategory.ipady = 5;
		gbc_lblCategory.weightx = 0.1;
		gbc_lblCategory.anchor = GridBagConstraints.WEST;
		gbc_lblCategory.fill = GridBagConstraints.BOTH;
		gbc_lblCategory.gridx = 0;
		gbc_lblCategory.gridy = 0;
		add(category, gbc_lblCategory);
		
		//itemID label
		html = "<html><body width='400px'>" + order.getContents() + "</body></html>";
		orderNo = new JLabel(html);
		orderNo.setHorizontalAlignment(JLabel.LEFT);
		orderNo.setFont(font);
		orderNo.setForeground(Color.BLACK);
		GridBagConstraints gbc_orderNo = new GridBagConstraints();
		gbc_orderNo.ipadx = 5;
		gbc_orderNo.ipady = 5;
		gbc_orderNo.weightx = 0.5;
		gbc_orderNo.anchor = GridBagConstraints.WEST;
		gbc_orderNo.fill = GridBagConstraints.BOTH;
		gbc_orderNo.gridx = 1;
		gbc_orderNo.gridy = 0;
		add(orderNo, gbc_orderNo);
		
		//itenName label
		html = "<html><body width='150px'>" + order.getStatus() + "</body></html>";
		orderContents = new JLabel();
		orderContents.setText(html);
		orderContents.setHorizontalAlignment(JLabel.LEFT);
		orderContents.setFont(new Font("Verdana Pro Cond Black", Font.BOLD, 28));
		orderContents.setForeground(Color.BLACK);
		GridBagConstraints gbc_orderContents = new GridBagConstraints();
		gbc_orderContents.ipadx = 5;
		gbc_orderContents.ipady = 5;
		gbc_orderContents.weightx = 0.15;
		gbc_orderContents.anchor = GridBagConstraints.WEST;
		gbc_orderContents.fill = GridBagConstraints.BOTH;
		gbc_orderContents.gridx = 2;
		gbc_orderContents.gridy = 0;
		add(orderContents, gbc_orderContents);
		
		//itemPrice label
		html = "<html><body width='150px'>P" + order.getTotal() + "</body></html>";
		orderTotal = new JLabel();
		orderTotal.setText(html);
		orderTotal.setHorizontalAlignment(JLabel.LEFT);
		orderTotal.setFont(font);
		orderTotal.setForeground(Color.BLACK);
		GridBagConstraints gbc_orderTotal = new GridBagConstraints();
		gbc_orderTotal.ipadx = 5;
		gbc_orderTotal.ipady = 5;
		gbc_orderTotal.weightx = 0.15;
		gbc_orderTotal.anchor = GridBagConstraints.WEST;
		gbc_orderTotal.fill = GridBagConstraints.BOTH;
		gbc_orderTotal.gridx = 3;
		gbc_orderTotal.gridy = 0;
		add(orderTotal, gbc_orderTotal);
		
		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new OrderProceedDialog((JFrame) SwingUtilities.getWindowAncestor(OrderPanel.this), order.getOrderNo());
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
	}
}
