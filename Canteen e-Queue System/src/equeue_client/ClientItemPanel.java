package equeue_client;

/**
 * Just a subclass of ItemPanel from shared_classes
 * The only difference is that this makes it so that
 * if it is clicked, an OrderAddDialog is opened.
 */
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import shared_classes.Item;
import shared_classes.ItemPanel;

public class ClientItemPanel extends ItemPanel {

	private static final long serialVersionUID = 1L;

	public ClientItemPanel(Item item) {
		super(item);
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(Client.order!=null) {
					new OrderAddDialog((JFrame) SwingUtilities.getWindowAncestor(ClientItemPanel.this), item);
				}
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
