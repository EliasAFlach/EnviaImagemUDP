package enviaimagemudp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
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

/**
 *
 * @author elias
 */
public class EnviaImagemUDP extends javax.swing.JFrame {

    private static int BUFFER_SIZE = 65507;
    private boolean capturar = false;
    private boolean receber = false;
    private BufferedImage image;

    public EnviaImagemUDP() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCapturar = new javax.swing.JButton();
        btnEnviar = new javax.swing.JButton();

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCapturar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEnviar)
                .addContainerGap(794, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnviar)
                    .addComponent(btnCapturar))
                .addContainerGap(635, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCapturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapturarActionPerformed
        capturar = true;
        repaint();
    }//GEN-LAST:event_btnCapturarActionPerformed

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed


    }//GEN-LAST:event_btnEnviarActionPerformed

    private void send(byte[] buffer, int port, DatagramSocket datagramSocket) throws IOException, UnknownHostException {
        DatagramPacket packet;
        packet = new DatagramPacket(buffer, buffer.length,
                InetAddress.getLocalHost(), port);
        datagramSocket.send(packet);
    }

    private void enviar() {

        try {
            int port = 7788;
            DatagramSocket datagramSocket = new DatagramSocket();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "JPG", baos);
            baos.flush();

            DatagramPacket packet;
            byte[] buffer;

            int sizePacket = (baos.toByteArray().length / BUFFER_SIZE);
            int rest = (baos.toByteArray().length % BUFFER_SIZE);

            
            int count = 0;
            for (int i = 0; i < baos.toByteArray().length; i += BUFFER_SIZE) {
                buffer = Arrays.copyOfRange(baos.toByteArray(), i, i + BUFFER_SIZE);
                send(buffer, port, datagramSocket);
                count++;
            }

            System.out.println("Pacote --->" + count);

            
            buffer = Arrays.copyOfRange(baos.toByteArray(), baos.toByteArray().length - rest, baos.toByteArray().length);
            send(buffer, port, datagramSocket);

        } catch (IOException ex) {
            Logger.getLogger(EnviaImagemUDP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void paint(Graphics g) {
        if (capturar) {
            try {
                int scale = 1;
                Robot r = new Robot();

                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

                int telaX = (int) screen.getWidth();
                int telaY = (int) screen.getHeight();
                image = r.createScreenCapture(new Rectangle(telaX, telaY));

                for (int y = 0; y < telaY; y++) {
                    for (int x = 0; x < telaX; x++) {
                        g.setColor(new Color(image.getRGB(x, y)));
                        g.drawRect(100 + x * scale, 100 + y * scale, scale, scale);
                    }
                }
                
                
                enviar();
                

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EnviaImagemUDP enviaImagemUDP = new EnviaImagemUDP();
                enviaImagemUDP.setTitle("Client");
                enviaImagemUDP.setDefaultCloseOperation(EXIT_ON_CLOSE);
                enviaImagemUDP.setLocationRelativeTo(enviaImagemUDP.rootPane);
                enviaImagemUDP.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapturar;
    private javax.swing.JButton btnEnviar;
    // End of variables declaration//GEN-END:variables
}
