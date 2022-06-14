package equeue_server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import shared_classes.Order;

/**
 * A thread responsible for receiving Orders from
 * clients. This runs continously and createes a
 * seperate thread for each request from clients.
 */
public class OrderThread extends Thread {

	ServerSocket server = null;
	boolean firstTry = true;
	
	//inner loops and try to allow multiple clients to send orders at once
	public void run() {
		try {
			server = new ServerSocket(Server.SERVER_PORT, 50, InetAddress.getByName(Server.host));
			firstTry = false;

			while(true) {
				Socket client = server.accept();
				Thread t = new Thread() {
					public void run() {
						try (
							BufferedWriter out = new BufferedWriter(new PrintWriter(client.getOutputStream(), true));	//Returns an order number to client
							ObjectInputStream in = new ObjectInputStream(client.getInputStream());						//Receives Order objects
						){
							try {
								Order order =  (Order) in.readObject();		//read order object from client
								int ord_num = ++Server.order_number;		//get incremented order number
								out.write(Integer.toString(ord_num));		//send order number to client
								out.flush();								//FLUUUUUUUUSH
								order.setOrderNo(ord_num);					//set order number of order object
								Server.orders.put(ord_num, order);			//put the order on orders HashMap
								Server.updateOrders();
							} catch (ClassNotFoundException e) {
								//do nothing
							}
						} catch (IOException e) {
							//do nothing
						}
					}
				};
				t.start();
			}
		} catch (IOException e) {
			if(firstTry) {
				System.exit(0);
			}
			JOptionPane.showMessageDialog(null, "Can't open server", "Server Failed", JOptionPane.WARNING_MESSAGE);
			
		}
	}
	
	public void interrupt() {
		super.interrupt();
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
