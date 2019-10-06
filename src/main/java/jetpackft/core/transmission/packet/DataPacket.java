package jetpackft.core.transmission.packet;

import jetpackft.core.reference.TransmissionConstants;
import jetpackft.core.utility.PacketUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A JFT data packet. This packet outlines a data packet - packets used in file transfer.
 */
public class DataPacket implements Packet {

    /**
     * The location of the packet. This is where it is in the sequence of
     * packets sent to the receiver.
     */
    public long packetLocation;
    /**
     * This is the transmission that this packet belongs in.
     */
    public UUID transmissionID;

    /**
     * This is the total length of the packet in bytes.
     */
    public long dataLength;
    /**
     * This is the total byte content of the packet.
     */
    public byte[] dataContents;

    /**
     * Create a new data packet.
     *
     * @param _transmissionID The ID of the Transmission this packet is a part of.
     * @param _packetLocation The location of the packet in sequencing. This helps in case a packet didn't send correctly.
     * @param data The data that this packet contains.
     */
    public DataPacket(UUID _transmissionID, long _packetLocation, byte[] data) {
        transmissionID = _transmissionID;
        packetLocation = _packetLocation;
        dataContents = data;
        dataLength = data.length;
    }

    public DataPacket() {}

    @Override
    public byte[] constructFlushData() {
        List<Byte> flushData = new ArrayList<>();

        flushData.add((byte) TransmissionConstants.CC_PACKET_START);
        byte[] packetLocationBytes = String.valueOf(packetLocation).getBytes();
        for (byte b : packetLocationBytes)
            flushData.add(b);
        flushData.add((byte) TransmissionConstants.CC_PACKET_DELIMITER);
        byte[] dataLengthBytes = String.valueOf(dataLength).getBytes();
        for (byte b : dataLengthBytes)
            flushData.add(b);
        flushData.add((byte) TransmissionConstants.CC_PACKET_DELIMITER);
        byte[] dataBytes = PacketUtility.encodePackets(dataContents);
        for (byte b : dataBytes)
            flushData.add(b);
        flushData.add((byte) TransmissionConstants.CC_PACKET_END);

        byte[] flushDataByteArray = new byte[flushData.size()];
        for (int bi = 0; bi < flushData.size(); bi++) {
            flushDataByteArray[bi] = flushData.get(bi);
        }

        return flushDataByteArray;
    }

    @Override
    public String getSafeName() {
        return transmissionID.toString().substring(0, 5) + "#" + packetLocation;
    }
}
