package app.enviaimagemudp;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

public class ReceiveImage extends JPanel implements Runnable {

    private DatagramSocket datagramSocket;
    private BufferedImage img;
    Socket server;
    JFrame frame = new JFrame();
    JLabel label = new JLabel();

    public ReceiveImage(int port) throws IOException, SQLException, ClassNotFoundException, Exception {
        datagramSocket = new DatagramSocket(port);
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
                    count = 1;
                    packets.clear();
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
                    frame.pack();

                    if (packet.getLength() == Util.HEADER_STOP) {
                        System.out.println("fim transmissao");
                        packets.clear();
                    }

                    repaint();

                }

                //  Thread.sleep(10);
            } catch (Exception ex) {
                System.out.println(ex);
                packets.clear();
                repaint();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR);

        g2.drawImage(img, 0, 0, frame);
    }

}
