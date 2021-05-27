package enviaimagemudp;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author elias
 */
public class EnviaImagemUDP extends JFrame implements Runnable {

    private boolean capturar = false;
    private BufferedImage image;

    public EnviaImagemUDP() {
        initComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCapturar = new javax.swing.JButton();
        btnEnviar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnCapturar.setText("Capturar");
        btnCapturar.setName(""); // NOI18N
        btnCapturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapturarActionPerformed(evt);
            }
        });

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCapturar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEnviar))
                    .addComponent(jLabel1))
                .addContainerGap(1325, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnviar)
                    .addComponent(btnCapturar))
                .addGap(77, 77, 77)
                .addComponent(jLabel1)
                .addContainerGap(657, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCapturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapturarActionPerformed
        capturar = true;
        repaint();
    }//GEN-LAST:event_btnCapturarActionPerformed

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        splitImageAndSend();
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void send(byte[] buffer) throws IOException, UnknownHostException {
        try ( DatagramSocket datagramSocket = new DatagramSocket()) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
                    InetAddress.getLocalHost(), Util.PORT);
            datagramSocket.send(packet);
        }
    }

    private void splitImageAndSend() {

        try {

            ByteArrayOutputStream baos = imageToByteArrays();
            int sizeImage = baos.toByteArray().length;
            int rest = (sizeImage % Util.BUFFER_SIZE);
            int sizePackets = (sizeImage - rest);

            byte[] buffer = new byte[Util.HEADER_START];
            send(buffer);

            for (int i = 0; i < sizePackets; i += Util.BUFFER_SIZE) {
                buffer = fillBuffer(baos, i, i + Util.BUFFER_SIZE);
                send(buffer);
            }

            buffer = fillBuffer(baos, sizePackets, sizeImage);
            send(buffer);

            buffer = new byte[Util.HEADER_STOP];
            send(buffer);

        } catch (IOException ex) {
            Logger.getLogger(EnviaImagemUDP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private byte[] fillBuffer(ByteArrayOutputStream baos, int sizePackets, int sizeImage) {
        return Arrays.copyOfRange(baos.toByteArray(), sizePackets, sizeImage);
    }

    private ByteArrayOutputStream imageToByteArrays() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "JPG", baos);
        baos.flush();
        return baos;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        while (true) {
            try {
                int scale = 1;
                Robot r = new Robot();
                int telaX = (int) Util.SCREEN.getWidth();
                int telaY = (int) Util.SCREEN.getHeight();
                image = r.createScreenCapture(new Rectangle(telaX, telaY));

                /*for (int y = 0; y < telaY; y+=120) {
                    for (int x = 0; x < telaX; x += 120) {
                        g2.setColor(new Color(image.getRGB(x, y)));
                        g2.drawRect(0 + x * scale, 0 + y * scale, scale, scale);
                    }
                }*/
                g2.drawImage(image, 0, 0, this);
                splitImageAndSend();
                Thread.sleep(1000);
            } catch (AWTException e) {
                System.out.println(e.getMessage());
            } catch (InterruptedException ex) {
                Logger.getLogger(EnviaImagemUDP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> {
            EnviaImagemUDP enviaImagemUDP = new EnviaImagemUDP();
            enviaImagemUDP.setTitle("");
            enviaImagemUDP.setDefaultCloseOperation(EXIT_ON_CLOSE);
            enviaImagemUDP.setLocationRelativeTo(enviaImagemUDP.rootPane);
            enviaImagemUDP.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapturar;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        repaint();
    }
}
