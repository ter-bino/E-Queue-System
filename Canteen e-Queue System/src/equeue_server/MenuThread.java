package equeue_server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;


/**
 * A thread that opens a server for obtaining the updated menu
 * If a client connects to this server, they will recieve a
 * menu object with updated contents.
 */
public class MenuThread extends Thread {
	
	ServerSocket server = null;
	
	//infinitely keep this socketserver open
	public void run() {
		while(true) {
			try {
				server = new ServerSocket(Server.MENU_PORT, 1, InetAddress.getByName(Server.host));
	
				while(true) {
					Socket client = server.accept();
					Thread t = new Thread() {
						public void run() {
							try (
								ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
							){
								out.writeObject(Server.menu); // send updated menu object to client
							} catch (IOException e) {
								//do nothing
							}
						}
					};
					t.start();
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Can't open menu server", "Server Failed", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	//close the server if this thread is interrupted
	public void interrupt() {
		super.interrupt();
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
