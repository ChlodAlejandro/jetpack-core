package jetpackft.core.transmission;

import jetpackft.core.transmission.packet.DataPacket;

import java.util.UUID;

public class Transmission {

    /**
     * The destination of the transmission.
     */
    public TransmissionDestination destination;
    /**
     * The transmission ID
     */
    public UUID id = UUID.randomUUID();
    /**
     * The total amount of packets to be sent.
     */
    public long packetTotal;
    /**
     * The standard length of the packets.
     */
    public int packetLength;
    /**
     * The array of packets.
     */
    public transient DataPacket[] packets;

    public String getFlushData() {
        return id.toString();
    }

    public Transmission(TransmissionDestination transmissionDestination) {
        destination = transmissionDestination;
    }
}
