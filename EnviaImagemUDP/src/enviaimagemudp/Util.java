package enviaimagemudp;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author Magaiver
 */
public class Util {
    public static int BUFFER_SIZE = 20000;
    public static final int PORT = 7788;
    public static final int HEADER_START = 1;
    public static final int HEADER_STOP = 0;
    public static Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
}