package equeue_client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * A dialog that shows up upon sending an order to the server
 * Displays the order number sent back by the server.
 */
public class OrderNumberDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JFrame headComponent;
	private JPanel buttonPane;
	private JButton okButton;
	private JLabel headerLabel;
	private JPanel mainContent;
	private JLabel lblNotice;
	private JLabel lblNum;

	/**
	 * Create the dialog.
	 */
	public OrderNumberDialog(JFrame headComponent, int orderNum) {
		super(headComponent, ModalityType.APPLICATION_MODAL);
		setUndecorated(true);
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// nothing, just go on with default LAF
		}
		if(headComponent!=null) {
			this.headComponent = headComponent;
		}
		setSize(312, 218);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.GRAY);
		contentPanel.setBorder(new LineBorder(Color.BLACK, 3, true));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			headerLabel = new JLabel("ORDER SENT");
			headerLabel.setForeground(Color.WHITE);
			headerLabel.setOpaque(true);
			headerLabel.setBackground(Color.DARK_GRAY);
			headerLabel.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 18));
			headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(headerLabel, BorderLayout.NORTH);
		}
		{
			mainContent = new JPanel();
			mainContent.setBorder(new MatteBorder(3, 6, 3, 6, (Color) Color.DARK_GRAY));
			mainContent.setBackground(Color.GRAY);
			contentPanel.add(mainContent, BorderLayout.CENTER);
			mainContent.setLayout(new BorderLayout(0, 0));
			{
				lblNotice = new JLabel("REMEMBER YOUR ORDER NUMBER");
				lblNotice.setFont(new Font("Tahoma", Font.PLAIN, 18));
				lblNotice.setForeground(Color.GREEN);
				lblNotice.setHorizontalAlignment(SwingConstants.CENTER);
				mainContent.add(lblNotice, BorderLayout.NORTH);
			}
			{
				lblNum = new JLabel(orderNum + "");
				lblNum.setForeground(Color.GREEN);
				lblNum.setHorizontalAlignment(SwingConstants.CENTER);
				lblNum.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 99));
				mainContent.add(lblNum, BorderLayout.CENTER);
			}
		}
		{
			buttonPane = new JPanel();
			buttonPane.setBorder(new LineBorder(Color.BLACK, 3, true));
			buttonPane.setBackground(Color.DARK_GRAY);
			FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.RIGHT);
			buttonPane.setLayout(fl_buttonPane);
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 14));
				okButton.setForeground(Color.WHITE);
				okButton.setBackground(Color.BLACK);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		FrameDragListener frameDragListener = new FrameDragListener(this);
        addMouseListener(frameDragListener);
        addMouseMotionListener(frameDragListener);
        setLocationRelativeTo(headComponent);
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
