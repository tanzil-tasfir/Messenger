import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame {

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;


    public Server() {
    super("Tanzil's Messsenger");
            userText = new JTextField();
            userText.setEditable(false);
            userText.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            sendMessage(e.getActionCommand());
                            userText.setText("");
                        }
                    }
            );
            add(userText, BorderLayout.NORTH);
            chatWindow = new JTextArea();
            add(new JScrollPane(chatWindow));
            setSize(300,150);
            setVisible(true);
    }
    //Setup and run the server
    public void startRunning(){
        try{
            server = new ServerSocket(6789, 100);
            while(true){
                try{
                 // connect and have conversation
                 waitForConnection();
                 setupStreams();
                 whileChatting();

                }
                catch(IOException e){
                    showMessage("\n Server ended the connection");
                }
                finally {
                    closeCrap();
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    // wait for your connection
    private void waitForConnection() throws IOException{
        showMessage("Waiting for someone to connect... \n");
        connection = server.accept();
        showMessage("Now connected to "+connection.getInetAddress().getHostName());

    }

    // get stream to receive Data
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are now Setup");
    }

    // During the chat conversation

    private void whileChatting() throws IOException{
        String message = "You are now connected";
        sendMessage(message);
        ableToType(true);
        do{
        // have a conversation
        try{
            message = (String) input.readObject();
            showMessage("\n"+ message);
        }
        catch (ClassNotFoundException classNotFoundException){
            showMessage("\n wtf User send -_- can't be read ");
        }
        }
        while (!message.equals("CLIENT - END"));
    }
    // closing down the streams and sockets after chatting

    private void closeCrap(){
        showMessage("\n Closing connection... \n");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close(); //closes the socket
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    // Send a message to client
    private void sendMessage(String message){
        try{
            output.writeObject("USER - "+ message);
            output.flush();
            showMessage("\n User - " + message);
        }
        catch (IOException ioException){
            chatWindow.append("\n ERROR: CAN'T SEND THE MESSAGE");
        }
    }

    // Updates chat window
    private void showMessage(final String text){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        chatWindow.append(text);
                    }
                }
        );
    }

    // giving typing ability to user
    private void ableToType(final boolean tof){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        userText.setEditable(tof);
                    }
                }
        );
    }
}