package equeue_server;

/**
 * This class extends ItemPanel from the shared_classes.
 * The only difference made here is that this class can
 * be clicked to directly edit or remove the show item
 * by lunching a MenuEditRemoveDialog
 */
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import shared_classes.Item;
import shared_classes.ItemPanel;

public class ServerItemPanel extends ItemPanel{
	private static final long serialVersionUID = 1L;

	public ServerItemPanel(Item item) {
		super(item);
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new MenuEditRemoveDialog((JFrame) SwingUtilities.getWindowAncestor(ServerItemPanel.this), item);
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
