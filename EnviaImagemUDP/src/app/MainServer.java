package app;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import app.audio.*;
import app.enviaimagemudp.ReceiveImage;
import app.enviaimagemudp.Util;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFrame;

public class MainServer {

    public static void main(String[] args) throws Exception {

        Runnable audioServer = () -> {
            try {
                Audio audio = new Audio();
                
                SourceDataLine speakers;
                DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audio.getFormat());
                
                speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                speakers.open(audio.getFormat());
                
                DatagramSocket socket = new DatagramSocket(8080);
                byte[] data = new byte[speakers.getBufferSize() / 5];
                speakers.start();
                while (true) {
                    DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                    socket.receive(receivePacket);
                    speakers.write(data, 0, data.length);
                }
            } catch (LineUnavailableException ex) {
            } catch (SocketException ex) {
            } catch (IOException ex) {
            }
        };
        
        ReceiveImage server = new ReceiveImage(Util.PORT);
        JFrame frame = new JFrame("");
        frame.add(server);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(server);
        frame.setVisible(true);

        Thread t = new Thread(server);
        t.start();
        
        
        
        
        
        
        
        
        Thread audioThread = new Thread(audioServer);
        audioThread.start();
        
        
        
        
        

    }
}
