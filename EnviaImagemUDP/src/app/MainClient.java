package app;

import app.audio.SendAudio;
import app.enviaimagemudp.SendImage;

/**
 *
 * @author user
 */
public class MainClient {

    public static void main(String[] args) {
//        Runnable sendImageRunnable = () -> {
//            
//            SendImage enviaImagemUDP = ;
//
//            
//        };
//        
        Thread audioThread = new Thread(new SendAudio());
        audioThread.start();

        Thread imageThread = new Thread(new SendImage());
        imageThread.start();

    }
}
