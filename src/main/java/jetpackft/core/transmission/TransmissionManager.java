package jetpackft.core.transmission;

import com.google.gson.Gson;
import jetpackft.core.connection.Connection;
import jetpackft.core.io.Log;
import jetpackft.core.reference.TransmissionConstants;
import jetpackft.core.transmission.packet.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;

/**
 * The class used for managing transmissions. The TransmissionManager
 * can be instantiated to connect to multiple sockets at the same time.
 */
public class TransmissionManager {

    /**
     * The tag that this class uses for logging.
     */
    private static final String LOG_TAG = "TransmissionManager";

    /**
     * The length of a DataPacket.
     */
    private static int packetLength = TransmissionConstants.PACKET_LENGTH;

    /**
     * The Log of this TransmissionManager.
     */
    private Log log;
    /**
     * The Connection of this TransmissionManager.
     */
    private Connection targetConnection;

    /**
     * Create a new TransmissionManager.
     *
     * @param _log The Log that this TransmissionManager will use.
     * @param destination The TransmissionDestination for this TransmissionManager
     */
    public TransmissionManager(Log _log, TransmissionDestination destination) {
        log = _log;

        log.appendToLog(Level.INFO, LOG_TAG, "Creating connection to target...");

        try {
            targetConnection = new Connection(destination);
            log.appendToLog(Level.FINE, LOG_TAG, "target: " + (new Gson()).toJson(targetConnection));
            targetConnection.connectSocket();
        } catch (IOException e) {
            log.appendToLog(Level.SEVERE, LOG_TAG, "Connection failed. Transfers cannot continue.");
            e.printStackTrace();
        }
    }

    /**
     * Set the length of a DataPacket.
     * @param length The new length of one DataPacket.
     */
    public void setPacketLength(int length) {
        packetLength = length;
    }

    /**
     * Send a file through the connection.
     *
     * @param file The File to send to the target.
     * @return `true` if the file was sent successfully. `false` if otherwise.
     */
    public boolean sendFile(File file) {
        long fileLength = file.length();
        
        if (!targetConnection.socket.isConnected()) {
            log.appendToLog(Level.WARNING, LOG_TAG, "Socket unavailable. (Check for connection?)");
            return false;
        }

        log.appendToLog(Level.FINE, LOG_TAG, "Sending file: " + file.getName());

        log.appendToLog(Level.FINE, LOG_TAG, "Check connection status... ");
        try {
            if (targetConnection.socket == null || targetConnection.socket.isClosed()) {
                log.appendToLog(Level.FINE, LOG_TAG, "Connection: BAD. Attempting reconnection...");
                targetConnection.connectSocket();
            } else
                log.appendToLog(Level.FINE, LOG_TAG, "Connection: OK");
        } catch (IOException e) {
            log.appendToLog(Level.SEVERE, LOG_TAG, "Connection failed. Transfer aborted.");
            e.printStackTrace();
            return false;
        }

        Transmission transmission = new Transmission(targetConnection.transmissionDestination);
        transmission.packetLength = packetLength;

        while (fileLength / transmission.packetLength > (long) 214748647) {
            transmission.packetLength *= 2;
        }

        log.appendToLog(Level.FINE, LOG_TAG, "Total packet length: " + transmission.packetLength);

        try {
            DataOutputStream socketOut = new DataOutputStream(targetConnection.socket.getOutputStream());

            log.appendToLog(Level.FINE, LOG_TAG, "Sending file information packet...");
            writePacketToStream(socketOut, new FileInformationPacket(transmission, file));

            FileInputStream fis = new FileInputStream(file);

            int packetCount = 0;
            long sentCount = 0;
            long lastCheck = System.currentTimeMillis();

            log.appendToLog(Level.FINE, LOG_TAG, "Sending data...");
            while (fis.available() > 0) {
                DataPacket packet;
                byte[] data;

                int byteCount = Math.max(0, Math.min(packetLength, fis.available()));
                data = new byte[byteCount];

                //noinspection ResultOfMethodCallIgnored
                fis.read(data);

                sentCount += data.length;
                packet = new DataPacket(transmission.id, ++packetCount, data);

                writePacketToStream(socketOut, packet);

                if (System.currentTimeMillis() - lastCheck > 1000) {
                    lastCheck = System.currentTimeMillis();
                    log.appendToLog(Level.INFO, LOG_TAG, String.format("%.2f%s of \"%s\" sent.",
                            ((double) sentCount / fileLength) * 100.0f,
                            "%",
                            file.getName()));
                }
            }

            log.appendToLog(Level.INFO, LOG_TAG, String.format("100.00%s of \"%s\" sent.",
                    "%",
                    file.getName()));

            log.appendToLog(Level.FINE, LOG_TAG, "Sending end of file byte.");
            writePacketToStream(socketOut, new FileEndPacket());
        } catch (IOException e) {
            log.appendToLog(Level.SEVERE, LOG_TAG, "Stream connection failed. Transfer aborted.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Close this TransmissionManager's socket.
     *
     * <p><b>WARNING</b>: This will prevent all future file transfers from occurring
     * through this manager! Make sure to stop sending files or renew the
     * TransmissionManager once this method has been called.</p>
     *
     * <p><b>WARNING</b>: This will immediately close the socket regardless if
     * there is a file being transferred. The client may interpret this as
     * the end of that file. The client is required to check for a closed
     * connection once the transmission end packet has been sent to
     * invalidate any file transfers occurring when the manager closed.</p>
     */
    public boolean closeTransmissionSocket() {
        log.appendToLog(Level.INFO, LOG_TAG, "Closing socket...");

        try {
            log.appendToLog(Level.FINE, LOG_TAG, "Sending transmission end packet.");
            writePacketToStream(new DataOutputStream(targetConnection.socket.getOutputStream()),
                    TransmissionEndPacket.getInstance());
            log.appendToLog(Level.FINE, LOG_TAG, "Sent transmission end packet. Closing socket...");
            targetConnection.socket.close();
            log.appendToLog(Level.INFO, LOG_TAG, "Socket closed.");
            return true;
        } catch (IOException e) {
            log.appendToLog(Level.SEVERE, LOG_TAG, "Failed to close socket/socket already closed.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Write a packet into the data stream.
     * @param dos The DataOutputStream to write the packet to.
     * @param packet The packet to write.
     * @throws IOException If writing failed.
     */
    private void writePacketToStream(DataOutputStream dos, Packet packet) throws IOException {
        byte[] packetFlushData = packet.constructFlushData();

        log.appendToLog(Level.ALL, LOG_TAG, "Writing packet to stream: " + packet.getSafeName());
        dos.write(packetFlushData, 0, packetFlushData.length);
        dos.flush();
    }

}
