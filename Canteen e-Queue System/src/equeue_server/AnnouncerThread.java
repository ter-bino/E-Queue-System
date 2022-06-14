package equeue_server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import shared_classes.Order;

/**
 * A thread that opens a server for keeping connection
 * with the announcer application, to continously pass 
 * status of order numbers.
 */
public class AnnouncerThread extends Thread {
	
	
	static PrintWriter out; //output stream to announcer
	static JFrame frame;	//every joptionpane that appears will have this frame as the parent
	
	
	private ServerSocket server = null;
	
	public AnnouncerThread(JFrame frame) {
		AnnouncerThread.frame = frame;
	}
	
	/**
	 * Overriding inherited method run()
	 * this run method keeps the serversocket for announcer
	 * open by waiting for a read() input that throws an
	 * exception upon disconenction
	 */
	@Override
	public void run() {
		while(true) {
			try (
				ServerSocket server = new ServerSocket(Server.ANNOUNCE_PORT, 1, InetAddress.getByName(Server.host));
			){
				while(true) {
					try(
						Socket announcer = server.accept();
					){
						//make the connections accesible outside of this method
						this.server = server;
						//sender object to client
						out = new PrintWriter(announcer.getOutputStream());
						
						//Update the announcer with existing orders that are processing and serving
						//upon connection
						for(Order order: ServerFunctions.refreshOrder(ServerFunctions.STATUS_PROCESSING)) {
							out.write(order.getOrderNo() + "\n");
							out.write("PROCESS\n");
							out.flush();
						}
						for(Order order: ServerFunctions.refreshOrder(ServerFunctions.STATUS_SERVING)) {
							out.write(order.getOrderNo() + "\n");
							out.write("PROCESS\n");
							out.write(order.getOrderNo() + "\n");
							out.write("SERVE\n");
							out.flush();
						}
						
						//throws IOException upon disconnection with the announcer
						announcer.getInputStream().read();
					}
				}
			} catch (IOException e) {
				out = null;
				JOptionPane.showMessageDialog(frame, "Connection with announcer is lost.. restarting connection..", "Connection Lost", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	

	/**
	 * Closes the server if this thread is interrupted
	 */
	public void interrupt() {
		super.interrupt();
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a request (or an update) to the announcer application
	 */
	public void sendToAnnouncer(JFrame frame, String request, String orderNo) {
		
		String newStatus = null;
		
		//picking new status
		switch(request) {
		case "PROCESS":
			newStatus = "PROCESSING";
			break;
		case "SERVE":
			newStatus = "SERVING";
			break;
		case "COMPLETE":
			newStatus = "COMPLETED";
			break;
		default:
			newStatus = Server.orders.get(Integer.parseInt(orderNo)).getStatus();
		}
		
		//just update the order status if announcer is not connected
		if(out==null) {
			
			Server.orders.get(Integer.parseInt(orderNo)).setStatus(newStatus);
			JOptionPane.showMessageDialog(frame, "Currently not connected with the announcer but order status is updated..", "Connection Lost", JOptionPane.WARNING_MESSAGE);
			return;
			
		//else if announcer is connected, send request to announcer
		} else {
			out.write(orderNo + "\n");
			out.write(request + "\n");
			out.flush();
	
			Server.orders.get(Integer.parseInt(orderNo)).setStatus(newStatus);
			JOptionPane.showMessageDialog(frame, "Set status of order no." + orderNo + " to "
										+ Server.orders.get(Integer.parseInt(orderNo)).getStatus().toLowerCase()
										, "Order status updated!", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
