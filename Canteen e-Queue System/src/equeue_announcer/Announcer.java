
package equeue_announcer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import java.util.HashSet;
import java.util.Scanner;
import java.net.Socket;
import java.io.IOException;

/**
 * This class is the Announcer application. This opens a connection
 * with the server which sends order numbers to be dispayed in this
 * application together with their status.
 * 	- process
 *  - serve
 *  - complete
 */
public class Announcer extends JFrame {
	
	private static final long serialVersionUID = 1L;

	//gets the screensize because app is FULLSCREEN
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	//GUI components
	static private JPanel contentPane, processPanel, servePanel, procNoPanel, servNoPanel;
	static private JLabel processLabel, serveLabel;
	static public Announcer frame;
	
	//HashSet collections to hold order numbers
	static private HashSet<String> processing = new HashSet<String>();
	static private HashSet<String> serving = new HashSet<String>();
	
	//thread to give a dynamic look to the "processing" and "serving" labels
	static Thread labelThread;
	static AnnounceThread at;
	

	/***
	 * Launch the application.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args){

			try {
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e1) {
				// just go on with default LAF
			}
		
		AnnounceThread.getServer();
		//run the GUI
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Announcer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		//connects to the server that gives order number updates
		at = new AnnounceThread();
		at.start();
		
	}

	/***
	 * Create the frame.
	 */
	public Announcer() {
		setTitle("E-Queue System: Announcer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setSize(screenSize.width, screenSize.height);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridLayout(0, 2, 0, 0));
		setContentPane(contentPane);
		
		//Panel that shows orders being processed
		processPanel = new JPanel();
		processPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 3, true));
		processPanel.setBackground(Color.GRAY);
		contentPane.add(processPanel);
		processPanel.setLayout(new BorderLayout(0, 0));
		
		//"Processing..."
		processLabel = new JLabel("Processing...");
		processLabel.setForeground(Color.DARK_GRAY);
		processLabel.setFont(new Font("Segoe Print", Font.BOLD | Font.ITALIC, 70));
		processPanel.add(processLabel, BorderLayout.NORTH);
		
		//panel that cointains the "numbers" itself (Processing)
		procNoPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) procNoPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(40);
		flowLayout.setHgap(40);
		procNoPanel.setBackground(Color.LIGHT_GRAY);
		procNoPanel.setBorder(new LineBorder(Color.DARK_GRAY, 4));
		processPanel.add(procNoPanel, BorderLayout.CENTER);

		//Panel that shows orders being served
		servePanel = new JPanel();
		servePanel.setBackground(Color.GRAY);
		servePanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 3));
		contentPane.add(servePanel);
		servePanel.setLayout(new BorderLayout(0, 0));
		
		//"Serving..."
		serveLabel = new JLabel("Serving...");
		serveLabel.setForeground(Color.GREEN);
		serveLabel.setFont(new Font("Segoe Print", Font.BOLD | Font.ITALIC, 70));
		servePanel.add(serveLabel, BorderLayout.NORTH);

		//panel that cointains the "numbers" itself (Serving)
		servNoPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) servNoPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		flowLayout_1.setVgap(40);
		flowLayout_1.setHgap(40);
		servNoPanel.setBorder(new LineBorder(Color.DARK_GRAY, 4));
		servNoPanel.setBackground(Color.LIGHT_GRAY);
		servePanel.add(servNoPanel, BorderLayout.CENTER);
		
		//Thread with an infinite loop that gives dynamic look to the announcer
		labelThread = new Thread() {
			public void run() {
				while(true) {
					try {
						Thread.sleep(500);
						processLabel.setText("Processing..");
						serveLabel.setText("Serving...");
						Thread.sleep(500);
						processLabel.setText("Processing...");
						serveLabel.setText("Serving");
						Thread.sleep(500);
						processLabel.setText("Processing");
						serveLabel.setText("Serving.");
						Thread.sleep(500);
						processLabel.setText("Processing.");
						serveLabel.setText("Serving..");
						if( processLabel.getForeground() == Color.DARK_GRAY) {
							processLabel.setForeground(Color.GREEN);
							serveLabel.setForeground(Color.DARK_GRAY);
						} else {
							processLabel.setForeground(Color.DARK_GRAY);
							serveLabel.setForeground(Color.GREEN);
						}
					} catch (InterruptedException e) {
						//do nothing
					}
				}
			}
		};
		labelThread.start();
	}
	
	//manage what happens when this application(frame) is closed
	public void dispose(){
		super.dispose();
		labelThread.interrupt();
		at.interrupt();
	}

	/**
	 * static method that processes requests to update
	 * processing and serving:
	 * 
	 * @param request	-	should contain either:
	 * 							"PROCESS"
	 * 							"SERVE"
	 * 							"COMPLETE"
	 * 
	 * @param number	-	the order number whose status is being updated...
	 */
	public static void processRequest(String request, String number) {
		
		//this try catch is making sure we only process numbers
		try {
			Integer.parseInt(number);
		} catch(NumberFormatException e) {
			return;
		}
		
		//actions depending on request
		if(request.toUpperCase().equals("PROCESS")) {
			if(!serving.contains(number)){
				processing.add(number);
			}
		} else if(request.toUpperCase().equals("SERVE")) {
			if(processing.contains(number)) {
				processing.remove(number);
				serving.add(number);
			}
		} else if(request.toUpperCase().equals("COMPLETE")) {
			if(serving.contains(number)) {
				serving.remove(number);
			}
		}
		
		//update the content of the frame/application
		updateContents();
		
	}
	
	/**
	 * Resets the content of the Announcer..
	 * This is called whenever there's a
	 * reconnection after an interruption.
	 */
	public static void reset() {
		processing.clear();
		serving.clear();
		updateContents();
	}

	/**
	 * updates the list of order numbers being
	 * process and being served that are displayed
	 * on the GUI
	 */
	private static void updateContents() {
		
		//Clear the display
		procNoPanel.removeAll();
		servNoPanel.removeAll();
		
		//then update
		for(String orderNo: processing) {
			JLabel procNo = new JLabel("[" + orderNo + "]");
			procNo.setForeground(Color.decode("#453d3d"));
			procNo.setFont(new Font("Segoe Script", Font.BOLD, 55));
			procNoPanel.add(procNo);
		}
		for(String orderNo: serving) {
			JLabel servNo = new JLabel("[" + orderNo + "]");
			servNo.setForeground(Color.decode("#2a9e00"));
			servNo.setFont(new Font("Segoe Script", Font.BOLD, 55));
			servNoPanel.add(servNo);
		}
		
		//to fix the display (update)
		frame.revalidate();
		frame.repaint();
		
	}
	
	
}

class AnnounceThread extends Thread{
	
	static final public int ANNOUNCE_PORT = 8890;	//the port where annoucer numbers are received
	static private String host;						//supposed to be final but needs user input
	
	public void run() {
		//loop to guarantee a connection with the server
		while(true)	{
			try (
				Socket socket = new Socket(host, ANNOUNCE_PORT);
				Scanner in = new Scanner(socket.getInputStream());
			){
				//loop to keep the announcer recieving updates from server
				while(true) {
					if(in.hasNextLine()) {
						//read requests from server
						String number = in.nextLine();
						String request = in.nextLine();
						Announcer.processRequest(request, number);
					}else {
						//checks if announcer is still connected with the server
						socket.getOutputStream().write(0);
					}
				}
			} catch (IOException e) {
				Announcer.reset();
				JOptionPane.showMessageDialog(null, "Connection with server is lost.. reconnecting..", "Connection Lost", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * static method that needs to be called first
	 * at the lunch of the program to get the host
	 * address from user input
	 */
	public static void getServer() {

		//loop that attempts to coonect to the server
		while(true) {
			try {
				//ask for host
				host = JOptionPane.showInputDialog("Enter server IP address:");
				//checks if host is valid to connect to, then close it right away.
				if(host!=null) {
					Socket client = new Socket(host, ANNOUNCE_PORT);
					client.close();
					break;
				} else {
					System.exit(0);
					return;
				}
			} catch (IOException e) {
				//if host address entered can't be connected to.. re-ask host address
				int resp = JOptionPane.showConfirmDialog(null, "Can't connect to that address... Retry?", "Failed", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
				if(resp != 0) {
					Announcer.frame.dispose();
					return;
				}
			}
		}
	}
}
