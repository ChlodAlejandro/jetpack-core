package jetpackft.core.transmission.packet;

import jetpackft.core.reference.TransmissionConstants;
import jetpackft.core.transmission.Transmission;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A FileInformationPacket, usually sent before DataPackets are sent.
 */
public class FileInformationPacket implements Packet {

    private File file;
    private Transmission transmission;

    public FileInformationPacket(Transmission fileTransmission, File fileToSend) {
        file = fileToSend;
        transmission = fileTransmission;
    }

    public String getFileHash() {
        String sha256Hash = null;
        try {
            InputStream is = new FileInputStream(file);
            sha256Hash = DigestUtils.sha256Hex(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sha256Hash != null ? sha256Hash : "";
    }

    @Override
    public byte[] constructFlushData() {
        return
            (TransmissionConstants.CC_FILE_INFO_START +
            transmission.id.toString() +
            TransmissionConstants.CC_PACKET_DELIMITER +
            file.getName() +
            TransmissionConstants.CC_PACKET_DELIMITER +
            getFileHash() +
            TransmissionConstants.CC_PACKET_DELIMITER +
            file.length() +
            TransmissionConstants.CC_FILE_INFO_END).getBytes();
    }

    @Override
    public String getSafeName() {
        return "PCK_INF_" + file.getName();
    }
}
