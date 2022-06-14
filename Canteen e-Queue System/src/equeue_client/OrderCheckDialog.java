package equeue_client;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

/**
 * A dialog that shows the contents of the existing order in the client app
 */
public class OrderCheckDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JFrame headComponent;
	private JPanel mainContent, buttonPanel;
	private JLabel lblOrderContent, headerLabel;
	private JButton btnOk;
	
	/**
	 * Create the dialog.
	 */
	public OrderCheckDialog(JFrame headComponent) {
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
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
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
		
		String html = "<html>" + "Your order contains: <br><br>" + Client.order.getContents() + "<br>=================<br>TOTAL PRICE: P" + Client.order.getTotal() + "</html>";
		lblOrderContent = new JLabel(html);
		lblOrderContent.setFont(new Font("Verdana Pro Cond Black", Font.BOLD | Font.ITALIC, 22));
		lblOrderContent.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblOrderContent = new GridBagConstraints();
		gbc_lblOrderContent.fill = GridBagConstraints.BOTH;
		gbc_lblOrderContent.gridwidth = 13;
		gbc_lblOrderContent.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrderContent.gridx = 2;
		gbc_lblOrderContent.gridy = 1;
		mainContent.add(lblOrderContent, gbc_lblOrderContent);
		
		headerLabel = new JLabel("ORDER CONTENT");
		headerLabel.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 22));
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(headerLabel, BorderLayout.NORTH);
		
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.GRAY);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		btnOk = new JButton("Okay");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		btnOk.setForeground(Color.WHITE);
		btnOk.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 18));
		btnOk.setBackground(Color.BLACK);
		buttonPanel.add(btnOk);
		
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

