//Networking4 -trying communicate between 2 side- currently whoever sends first succeeds
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Server extends JFrame {

	//new
	// textfield for entering message
		private JTextField textField = new JTextField();
	//new
		
	JTextArea textAreaServer = new JTextArea();
	private ServerSocket serverSocket;
	private Socket socket;
	private DataInputStream inputFromClient;
	private DataOutputStream outputToClient;

	//when starting this app, you'll need the GUI area and then start the socket connection
	public Server() {
		//new - panel to hold label and text field
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(new JLabel("Enter message: "), BorderLayout.WEST);
		p.add(textField, BorderLayout.CENTER);
		textField.setHorizontalAlignment(JTextField.LEFT);
		//new
		
		setLayout(new BorderLayout()); 
		textAreaServer.setFont(new Font("Dailog", Font.BOLD, 18));
		add(p, BorderLayout.NORTH); //new - must add to allow server to enter message
		add(new JScrollPane(textAreaServer), BorderLayout.CENTER);

		setTitle("Server");
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		textField.addActionListener(new TextFieldListenerServer()); //new -listen for client message
		
		try {
			// create a server socket
			serverSocket = new ServerSocket(8000);
			textAreaServer.append("Yo, Sithi's server is up!: " + new Date() + "\n");
			textAreaServer.append("Waiting for Connection...\n");

			// listen for a connection request
			socket = serverSocket.accept();
			textAreaServer.append("Connected!!\n");

			// create data input and output streams to send messages
			inputFromClient = new DataInputStream(socket.getInputStream());
			outputToClient = new DataOutputStream(socket.getOutputStream());


			while (true) {
				try {
					// receive radius from client
					String msg = inputFromClient.readUTF();
					textAreaServer.append("\nreceived from client: " + msg + "\n");

					// reassign message to local variable for reply to client
					String returnMSG = msg;

					// send back to the client
					//outputToClient.writeUTF(returnMSG);
					//textAreaServer.append("reply to client: " + returnMSG + "\n");
				}	
				
				catch (java.net.SocketException ex) {

					// listen for connection request
					textAreaServer.append("Waiting for connection...\n");
					socket = serverSocket.accept();
					textAreaServer.append("Connected!!\n");

					// create data input and output streams-- same as initial data streams above so can convert to method instead and call method
					inputFromClient = new DataInputStream(socket.getInputStream());
					outputToClient = new DataOutputStream(socket.getOutputStream());
				}
			}
		}	catch(IOException ex) {
			System.err.println(ex);
		}
	}

	
	//new - add ActionListener for TextField
	private class TextFieldListenerServer implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				// get the message from textfield
				String msg = textField.getText();

				// send message to client
				outputToClient.writeUTF(msg);
				outputToClient.flush();

				// get reply from client
				String responseMSG = inputFromClient.readUTF();

				// display message to text area
				textAreaServer.append("\nserver typed: " + msg + "\n");
				textAreaServer.append("reply from client: " + responseMSG + "\n");
			}

			catch (Exception ex) {
				System.err.println(ex);
			}
		}
	} 	
	//new
	
	// run the server side
	public static void main(String[] args) {
		new Server();
	}
}
