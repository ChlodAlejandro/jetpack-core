package jetpackft.server.tests;

import jetpackft.core.io.Log;
import jetpackft.core.transmission.TransmissionDestination;
import jetpackft.core.transmission.TransmissionManager;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static jetpackft.server.tests.utility.TestUtility.getTestResourceFile;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TransmissionManagerTest {

    private void sendFileWithTransmissionManager(String testFileName) throws UnknownHostException {
        Log log = null;
        try {
            log = new Log();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        log.appendToLog(Level.INFO, "TEST", "Creating connection...");
        TransmissionManager transManager = new TransmissionManager(log,
                new TransmissionDestination("LocalTest", InetAddress.getByName("127.0.0.1"), (short) 1988));
        log.appendToLog(Level.INFO, "TEST", "Sending file...");
        boolean successfulTransfer = transManager.sendFile(getTestResourceFile(log, testFileName));
        boolean successfulDisconnect = transManager.closeTransmissionSocket();
        assertTrue(successfulDisconnect && successfulTransfer);
    }

    @Test
    public void sendTestFile() throws UnknownHostException {
        sendFileWithTransmissionManager("12b_text.txt");
    }

    @Test
    public void sendSmallDummyFile( ) throws UnknownHostException {
        sendFileWithTransmissionManager("1mB_random.dummy");
    }

    @Test
    public void sendSmallImageFile() throws UnknownHostException {
        sendFileWithTransmissionManager("25kB_image.jpg");
    }

    @Test
    public void sendLargeTextFile() throws UnknownHostException {
        sendFileWithTransmissionManager("5300kB_words.txt");
    }

    @Test
    public void sendLargeDummyFile() throws UnknownHostException {
        sendFileWithTransmissionManager("128mB_random.dummy");
    }

    @Test
    public void findBestPacketSize() throws UnknownHostException {
        Log log = null;
        try {
            log = new Log();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        log.appendToLog(Level.INFO, "TEST", "Creating connection...");
        TransmissionManager transManager = new TransmissionManager(log,
                new TransmissionDestination("LocalTest", InetAddress.getByName("127.0.0.1"), (short) 1988));

        int[] packetSizes = new int[] {
                Short.MAX_VALUE // best value
        };

        File[] files = new File[] {
              getTestResourceFile(log, "512mB_random.dummy")
        };

        Map<String, Map<Integer, Long>> results = new HashMap<>();

        for (File file : files) {
            Map<Integer, Long> times = new HashMap<>();

            for (int packetSize : packetSizes) {
                log.appendToLog(Level.INFO, file.getName(), "Sending at packet size: " + packetSize + "B");
                transManager.setPacketLength(packetSize);
                long sendStart = System.currentTimeMillis();
                if (transManager.sendFile(file)) {
                    long duration = (System.currentTimeMillis() - sendStart);
                    log.appendToLog(Level.INFO, file.getName(), "Sent. Took " + duration + "ms.");
                    times.put(packetSize, duration);
                } else
                    log.appendToLog(Level.WARNING, file.getName(), "Failed to send. Took " + (System.currentTimeMillis() - sendStart) + "ms.");
            }

            results.put(file.getName(), times);
        }

        log.appendToLog(Level.INFO, "TEST", "Test complete.");
        for (Map.Entry<String, Map<Integer, Long>> entry : results.entrySet()) {
            log.appendToLog(Level.INFO, "TEST", entry.getKey() + ":");
            for (Map.Entry<Integer, Long> timing : entry.getValue().entrySet()) {
                log.appendToLog(Level.INFO, "TEST", "\t" + timing.getKey() + "B per packet: " + timing.getValue() + "ms");
            }
        }
    }

}
