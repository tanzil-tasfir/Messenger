import javax.swing.JFrame;
public class ServerTest {
    public static void main(String[] args){
        Server Tanzil = new Server();
        Tanzil.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Tanzil.startRunning();
    }
}
