package jetpackft.core.transmission;

import java.net.InetAddress;
import java.util.UUID;

public class TransmissionDestination {

    /**
     * The ID of the target device
     */
    public UUID id;
    /**
     * The display name of the target device.
     */
    public String displayName;
    /**
     * The IP address of the target device.
     */
    public InetAddress ipAddress;
    /**
     * The port of the socket of the target device.
     */
    public short port;

    public TransmissionDestination(UUID targetUUID, String _name, InetAddress _ipAddress, short _port) {
        id = targetUUID;
        displayName = _name;
        ipAddress = _ipAddress;
        port = _port;
    }

    public TransmissionDestination(String _name, InetAddress _ipAddress, short _port) {
        id = UUID.randomUUID();
        displayName = _name;
        ipAddress = _ipAddress;
        port = _port;
    }

}
