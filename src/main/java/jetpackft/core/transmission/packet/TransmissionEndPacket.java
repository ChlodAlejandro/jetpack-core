package jetpackft.core.transmission.packet;

import jetpackft.core.reference.TransmissionConstants;

public class TransmissionEndPacket implements Packet {

    private static TransmissionEndPacket instance;

    public static TransmissionEndPacket getInstance() {
        if (instance == null)
            instance = new TransmissionEndPacket();
        return instance;
    }

    @Override
    public byte[] constructFlushData() {
        return new byte[] { (byte) TransmissionConstants.CC_TRANSMISSION_END};
    }

    @Override
    public String getSafeName() {
        return "PCK_TRANS_END";
    }

}
