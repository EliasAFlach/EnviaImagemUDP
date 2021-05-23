package enviaimagemudp;

import java.awt.Graphics;
import java.awt.Graphics2D;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Server extends JPanel implements Runnable {

    private DatagramSocket datagramSocket;
    private BufferedImage img;
    Socket server;
    JFrame frame = new JFrame();
    JLabel label = new JLabel();

    public Server(int port) throws IOException, SQLException, ClassNotFoundException, Exception {
        datagramSocket = new DatagramSocket(port);
        /*frame.add(label);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(frame);
        frame.setDefaultCloseOperation(3);
        frame.setTitle("Servidor");
        frame.setVisible(true);}*/
    }

    @Override
    public void run() {
        List<Byte> packets = new ArrayList<>();
        int count = 0;

        while (true) {
            try {
                InputStream input;
                byte[] data = new byte[Util.BUFFER_SIZE];

                DatagramPacket packet = new DatagramPacket(data, data.length);
                datagramSocket.receive(packet);
                count = count + 1;
                byte[] buffer = packet.getData();

                System.out.println("Pacote n° " + count + " de tamanho " + packet.getLength());

                if (packet.getLength() == Util.HEADER_START) {
                    System.out.println("Inicio transmissão");
                } else {

                    for (int i = 0; i < buffer.length; i++) {
                        packets.add(buffer[i]);
                    }

                    byte[] newBuffer = new byte[packets.size()];

                    for (int i = 0; i < packets.size(); i++) {
                        newBuffer[i] = packets.get(i);
                    }

                    input = new ByteArrayInputStream(newBuffer);
                    img = ImageIO.read(ImageIO.createImageInputStream(input));
                    //label.setIcon(new ImageIcon(img));
                    frame.pack();

                    if (packet.getLength() == Util.HEADER_STOP) {
                        System.out.println("fim transmissao");
                        packets.clear();
                    }
                    repaint();
                }

                //Thread.sleep(1000);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(img, 0, 0, frame);
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, Exception {
        Server server = new Server(Util.PORT);

        JFrame frame = new JFrame("Server");
        frame.add(server);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(server);
        frame.setVisible(true);

        Thread t = new Thread(server);
        t.start();

    }
}
