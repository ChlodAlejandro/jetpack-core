package jetpackft.core.connection;

import jetpackft.core.transmission.TransmissionDestination;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

/**
 * A JFT connection.
 */
public class Connection {

    /**
     * The ID of the destination.
     */
    public UUID id;
    /**
     * The display name of the destination.
     */
    public String name;
    /**
     * The Socket of the connection.
     */
    public transient Socket socket;

    /**
     * The TransmissionDestination that represents this Connection.
     */
    public TransmissionDestination transmissionDestination;

    public Connection() {}

    public Connection(TransmissionDestination _transmissionDestination) throws IOException {
        id = UUID.randomUUID();

        name = _transmissionDestination.displayName;
        transmissionDestination = _transmissionDestination;
        connectSocket();
    }

    public Connection(String displayName, InetAddress destination, short port) throws IOException{
        id = UUID.randomUUID();

        name = displayName;
        transmissionDestination = new TransmissionDestination(id, name, destination, port);
        connectSocket();
    }

    /**
     * Connect the socket to a working endpoint.
     */
    public boolean connectSocket() throws IOException {
        if (socket != null && socket.isConnected()) {
            return true;
        } else {
            socket = new Socket(transmissionDestination.ipAddress, transmissionDestination.port);
            return socket.isConnected();
        }
    }

}
