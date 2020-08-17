import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.math.*;

/** Primitive chat client.
 * This client connects to a server so that messages can be typed and forwarded
 * to all other clients.  Try it out in conjunction with ImprovedChatServer.java.
 * You will need to modify / update this program to incorporate the secure
 * elements as specified in the Assignment description.  Note that the PORT used
 * below is not the one required in the assignment -- for your SecureChatClient
 * be sure to change the port that so that it matches the port specified for the
 * secure  server.
 * @author Sherif Khattab
 * Adapted from Dr. John Ramirez's CS 1501 Assignment 4
 */
public class ImprovedChatClient extends JFrame implements Runnable, ActionListener {

    public static final int PORT = 8765;

    ObjectInputStream myReader;
    ObjectOutputStream myWriter;
    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;
    String myName, serverName;
	Socket connection;
    SymCipher cipher = null;
    BigInteger key;
    byte[] name;

    public ImprovedChatClient () throws IOException
    {
        try {

        myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
        serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");
        InetAddress addr =
                InetAddress.getByName(serverName);
        connection = new Socket(addr, PORT);   // Connect to server with new
                                               // Socket

        myWriter =
             new ObjectOutputStream(connection.getOutputStream());
        myWriter.flush();
        myReader =
             new ObjectInputStream(connection.getInputStream());   // Get Reader and Writer

        BigInteger E = new BigInteger("" + myReader.readObject());
        BigInteger N = new BigInteger("" + myReader.readObject());
        String encType = "" + myReader.readObject();
        System.out.println("E: " + E);
        System.out.println("N: " + N);
        System.out.println("Encryption type: " + encType);

        if (encType.equals("Add"))
            cipher = new Add128();
        else
            cipher = new Substitute();

        key = new BigInteger(cipher.getKey());
        key = key.modPow(E, N);
        System.out.println("Key: " + key);
        myWriter.writeObject(key);
        myWriter.flush();

        name = cipher.encode(myName);
        myWriter.writeObject(name);   // Send name to Server.  Server will need
                                    // this to announce sign-on and sign-off
                                    // of clients
        myWriter.flush();

        this.setTitle(myName);      // Set title to identify chatter

        Box b = Box.createHorizontalBox();  // Set up graphical environment for
        outputArea = new JTextArea(8, 30);  // user
        outputArea.setEditable(false);
        b.add(new JScrollPane(outputArea));

        outputArea.append("Welcome to the Chat Group, " + cipher.decode(name) + "\n");

        inputField = new JTextField("");  // This is where user will type input
        inputField.addActionListener(this);

        prompt = new JLabel("Type your messages below:");
        Container c = getContentPane();

        c.add(b, BorderLayout.NORTH);
        c.add(prompt, BorderLayout.CENTER);
        c.add(inputField, BorderLayout.SOUTH);

        Thread outputThread = new Thread(this);  // Thread is to receive strings
        outputThread.start();                    // from Server

		addWindowListener(
                new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    {
                        try {
                            byte[] message = cipher.encode("CLIENT CLOSING");
                            myWriter.writeObject(message);
                            myWriter.flush();
                            System.exit(0);
                        }
                       catch (Exception ex) {
                           System.out.println("error");
                       }
                    }
                }
            );

        setSize(500, 200);
        setVisible(true);

        }
        catch (Exception e)
        {
            System.out.println("Problem starting client!");
        }
    }

    public void run()
    {
        while (true)
        {
             try {
                byte[] currMsg = (byte[])myReader.readObject();
			    outputArea.append(cipher.decode(currMsg) +"\n");
             }
             catch (Exception e)
             {
                System.out.println(e +  ", closing client!");
                break;
             }
        }
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e)
    {
        try {
            String currMsg = e.getActionCommand();      // Get input value
            byte[] msg = cipher.encode(myName + ":" + currMsg);
            inputField.setText("");
            myWriter.writeObject(msg);   // Add name and send it
            myWriter.flush();
            // to Server
        }
        catch (Exception ex) {
            System.out.println("error");
        }
    }


    public static void main(String [] args) throws IOException
    {
         ImprovedChatClient JR = new ImprovedChatClient();
         JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}
