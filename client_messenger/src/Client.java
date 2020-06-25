import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String serverIP;
    private Socket connection;

    // constructor GUI

    public Client(String host){
        super("Client");
        serverIP = host;
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
    // connect to server
    public void startRunning(){
        try{
            connectToServer();
            setupStreams();
            whileChatting();
        }
        catch (EOFException e){
            showMessage("\n Client terminated connection");
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            closeCrap();
        }

    }
    // connect to server

    private void connectToServer() throws IOException{
        showMessage("Attempting connection... \n");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("Connected to: " +connection.getInetAddress().getHostName());
    }
    // set up streams to send  and receive messages
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are good to go");

    }

    // while chatting method
    private void whileChatting() throws IOException{
        String message = "You are now connected";
        sendMessage(message);
        ableToType(true);

        do{
            try{
                message = (String) input.readObject();
                showMessage("\n "+ message);
            }
            catch(ClassNotFoundException e){
                showMessage("\n I don't know the object");
            }
        }
        while (!message.equals("SERVER - END"));
    }

    //close the streams and sockets
    private void closeCrap(){
        showMessage("\n closing crap down...");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    // send messages to server

    private void sendMessage(String message) {
        try {
            output.writeObject("CLIENT - " + message);
            output.flush();
            showMessage("\n CLIENT  -" + message);
        } catch (IOException e) {
            chatWindow.append("\n Something messes up sending message");
        }
    }

        // change or update chat window
        private void showMessage(final String m){
            SwingUtilities.invokeLater(
                    new Runnable() {
                        @Override
                        public void run() {
                            chatWindow.append(m);
                        }
                    }
            );
        }

        // gives user permission to type
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
