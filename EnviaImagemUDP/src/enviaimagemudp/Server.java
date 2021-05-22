package enviaimagemudp;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Server extends Thread {

    private static int BUFFER_SIZE = 65507;
    private DatagramSocket datagramSocket;
    private BufferedImage img;
    Socket server;
    JFrame frame = new JFrame();
    JLabel label = new JLabel();

    public Server(int port) throws IOException, SQLException, ClassNotFoundException, Exception {
        datagramSocket = new DatagramSocket(port);
        frame.add(label);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(frame);
        frame.setDefaultCloseOperation(3);
        frame.setTitle("Servidor");
        frame.setVisible(true);
    }

    public void run() {
        List<Byte> packets = new ArrayList<Byte>();
        int count = 0;

        while (true) {
            try {
                InputStream input;
                byte[] data = new byte[BUFFER_SIZE];
                
                DatagramPacket packet = new DatagramPacket(data, data.length);
                datagramSocket.receive(packet);
                count = count + 1;
                byte[] buffer = packet.getData();

                System.out.println("Pacote n° " + count + " de tamanho " + buffer.length);

                for (int i = 0; i < buffer.length; i++) {
                    packets.add(buffer[i]);
                }

               // if (count == 24) {
                    byte[] newBuffer = new byte[packets.size()];

                    for (int i = 0; i < packets.size(); i++) {
                        newBuffer[i] = packets.get(i);
                    }

                    input = new ByteArrayInputStream(newBuffer);
                    img = ImageIO.read(ImageIO.createImageInputStream(input));
                    label.setIcon(new ImageIcon(img));
                    frame.pack();
                  
                    Thread.sleep(10);
                    
                    
                //}

            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    public void receiveFile() {

    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, Exception {
        Thread t = new Server(7788);
        t.start();

    }
}
