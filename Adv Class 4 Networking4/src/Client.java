//Networking4 -trying communicate between 2 side- currently whoever sends first succeeds
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame {
	Socket socket = null;

	// textfield for entering message
	private JTextField textField = new JTextField();

	// textarea to display content
	private JTextArea textAreaClient = new JTextArea();

	// IO streams
	private DataOutputStream toServer;
	private DataInputStream fromServer;

	public Client() throws IOException {
		// panel to hold label and text field
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(new JLabel("Enter message: "), BorderLayout.WEST);
		p.add(textField, BorderLayout.CENTER);
		textField.setHorizontalAlignment(JTextField.LEFT);

		setLayout(new BorderLayout());
		add(p, BorderLayout.NORTH);
		add(new JScrollPane(textAreaClient), BorderLayout.CENTER);

		textField.addActionListener(new TextFieldListener());

		setTitle("Client");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true); // it is necessary to show the frame here!

		try {
			socket = new Socket("127.0.0.1",8000);
			//OR socket = new createSocket("127.0.0.1",8000);
			textAreaClient.setFont(new Font("Dialog", Font.PLAIN, 18));
			textAreaClient.append("Sithi's server is up now!\n");
			//System.out.println("local port: " + socket.getLocalPort());

			// create an input stream to receive data from server
			fromServer = new DataInputStream(socket.getInputStream());

			// create an output stream to send data to server
			toServer = new DataOutputStream(socket.getOutputStream());
		}

		catch (IOException ex) {
			textAreaClient.append(ex.toString() + "\n Yo, couldn't connect!");
		}
		
		//new
		while (true) {
			try {
				// receive radius from client
				String msg = fromServer.readUTF();
				textAreaClient.append("\nreceived from client: " + msg + "\n");

				// reassign message to local variable for reply to client
				String returnMSG = msg;

				// send back to the client
				//outputToClient.writeUTF(returnMSG);
				//textAreaServer.append("reply to client: " + returnMSG + "\n");
			}	
			
			catch (java.net.SocketException ex) {

				// listen for connection request
				textAreaClient.append("Waiting for connection...\n");
				//socket = serverSocket.accept();
				textAreaClient.append("Connected!!\n");

				// create data input and output streams-- same as initial data streams above so can convert to method instead and call method
				fromServer = new DataInputStream(socket.getInputStream());
				toServer = new DataOutputStream(socket.getOutputStream());
			}
		}
		//new
		
		
	}

	private class TextFieldListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				// get the message from textfield
				String msg = textField.getText();

				// send message to server
				toServer.writeUTF(msg);
				toServer.flush();

				// get reply from server
				String responseMSG = fromServer.readUTF();

				// display message to text area
				textAreaClient.append("\nclient typed: " + msg + "\n");
				textAreaClient.append("reply from server: " + responseMSG + "\n");
			}

			catch (Exception ex) {
				System.err.println(ex);
			}
		}
	}

	// run the client side app
	public static void main(String[] args) throws IOException {
		new Client();
	}
}
