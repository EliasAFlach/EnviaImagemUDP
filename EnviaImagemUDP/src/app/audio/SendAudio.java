package app.audio;

import java.io.IOException;
import javax.sound.sampled.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SendAudio implements Runnable {

    Audio audio = new Audio();

    @Override
    public void run() {
        try {
            TargetDataLine line;
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audio.getFormat());
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Not Supported");
                System.exit(1);
            }

            DatagramSocket socket = new DatagramSocket(8081);
            InetAddress IPAddress = InetAddress.getByName("localhost");

            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(audio.getFormat());

            //ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            byte[] data = new byte[line.getBufferSize() / 5];
            int totalBytesRead = 0;

            line.start();
            while (true) {
                numBytesRead = line.read(data, 0, data.length);
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 8080);
                totalBytesRead += numBytesRead;
                socket.send(sendPacket);
                //out.write(data, 0, numBytesRead);
                System.out.println(totalBytesRead);
            }

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (SocketException ex) {
        } catch (UnknownHostException ex) {
        } catch (IOException ex) {
        }

    }
}
