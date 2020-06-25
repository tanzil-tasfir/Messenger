import javax.swing.JFrame;

public class ClientTest {
    public static void main (String[] args){
    Client sakib;
    sakib = new Client("127.0.0.1");
    sakib.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    sakib.startRunning();
    }
}
