package enviaimagemudp;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Server extends Thread {

    private ServerSocket serverSocket;
    private BufferedImage img;
    Socket server;
    JFrame frame = new JFrame();
    JLabel label = new JLabel();

    public Server(int port) throws IOException, SQLException, ClassNotFoundException, Exception {
        serverSocket = new ServerSocket(port);
        frame.add(label);
        frame.setVisible(true);
    }

    public void run() {
        while (true) {
            try {
                server = serverSocket.accept();
                img = ImageIO.read(ImageIO.createImageInputStream(server.getInputStream()));
                label.setIcon(new ImageIcon(img));
                frame.pack();
            } catch (SocketTimeoutException st) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, Exception {
        Thread t = new Server(6066);
        t.start();
    }
}
