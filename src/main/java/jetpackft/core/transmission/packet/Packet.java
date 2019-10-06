package jetpackft.core.transmission.packet;

public interface Packet {

    /**
     * Construct the byte array to be flushed into the stream. The contents
     * of the flush data should depend on the contents of the packet.
     *
     * @return A byte array to be flushed to the network stream/file stream.
     */
    byte[] constructFlushData();

    /**
     * Get the safe name of this packet. This may be the packet name, packet
     * code, packet location, etc.
     *
     * @return The safe/normal name of this packet.
     */
    String getSafeName();

}
