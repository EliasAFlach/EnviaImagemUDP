package app.audio;

import javax.sound.sampled.AudioFormat;

/**
 * @author Magaiver Santos
 */
public class Audio {

    private AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
    private float rate = 44100.0f;
    private int sampleSize = 16;
    private int channels = 2;
    private int frameSize = 4;
    boolean bigEndian = true;
    private AudioFormat format;



    public AudioFormat getFormat() {
        return format = new AudioFormat(encoding, rate,
                sampleSize, channels, (sampleSize / 8) * channels,
                rate, bigEndian);
    }
}
