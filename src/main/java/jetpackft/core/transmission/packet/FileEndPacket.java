package jetpackft.core.transmission.packet;

import jetpackft.core.reference.TransmissionConstants;

public class FileEndPacket implements Packet {
    @Override
    public byte[] constructFlushData() {
        return new byte[] { (byte) TransmissionConstants.CC_FILE_END};
    }

    @Override
    public String getSafeName() {
        return "PCK_FILE_END";
    }
}
