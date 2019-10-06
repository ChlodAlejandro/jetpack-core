package jetpackft.server.tests;

import jetpackft.core.connection.Connection;
import jetpackft.core.io.Log;
import org.junit.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.logging.Level;

import static jetpackft.server.tests.utility.TestUtility.getTestResourceFile;
import static org.junit.Assert.fail;

public class NetworkStreamTest {

    @Test
    public void sendTestPlainTextRaw() throws IOException {
        Connection connection = new Connection("LocalTest", InetAddress.getByName("127.0.0.1"), (short) 1988);

        DataOutputStream dos = new DataOutputStream(connection.socket.getOutputStream());

        byte[] testText = "Hello World!".getBytes();
        dos.write(testText, 0, testText.length);
        dos.flush();
        dos.close();
    }

    @Test
    public void sendLongTestPlainTextRaw() throws IOException {
        Log log = null;
        try {
            log = new Log();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        log.appendToLog(Level.INFO, "TEST", "Creating connection...");
        Connection connection = new Connection("LocalTest", InetAddress.getByName("127.0.0.1"), (short) 1988);

        DataOutputStream dos = new DataOutputStream(connection.socket.getOutputStream());
        Scanner fileInput = new Scanner(getTestResourceFile(log, "5300kB_words.txt"));

        log.appendToLog(Level.INFO, "TEST", "Sending text...");
        int lines = 0;
        while (fileInput.hasNextLine()) {
            byte[] testText = (fileInput.nextLine() + "\n").getBytes();
            dos.write(testText, 0, testText.length);
            dos.flush();
            lines++;
            if (lines >= 466544 || lines % 1000 == 0) {
                log.appendToLog(Level.FINE, "TEST", "Sent " + lines + " lines.");
            }
        }
        dos.close();
    }

}
