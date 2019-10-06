package jetpackft.core.reference;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class outlines control characters used by Jetpack to transfer
 * files. These control characters may be changed in the future if the
 * control characters are deemed incompatible, hence there is a
 * control character set version code.
 */
public class TransmissionConstants {

    /**
     * The control character set version code.
     *
     * When connecting, the two devices should agree on character sets. If one
     * version is outdated, they must update to the latest character set before
     * continuing.
     *
     * The name is long for comedic purposes. Just call it "control set version".
     */
    public static final int CONTROL_CHARACTER_SET_VERSION = 0;

    // PACKET CONSTANTS

    /**
     * The packet start character (SOT/Start Of Text).
     */
    public static final char CC_PACKET_START = (char) 2;
    /**
     * The packet end character (EOT/End Of Text).
     */
    public static final char CC_PACKET_END = (char) 3;
    /**
     * The packed delimiter character (RS/Record Separator).
     */
    public static final char CC_PACKET_DELIMITER = (char) 30;

    /**
     * The length of one DataPacket.
     */
    public static final int PACKET_LENGTH = Short.MAX_VALUE / 16;

    // TRANSMISSION CONSTANTS

    /**
     * The character that marks the end of a transmission. (ETB/End of Transmission Block)
     */
    public static final char CC_TRANSMISSION_END = (char) 23;
    /**
     * The character that marks the start of a file. (FS/File Separator)
     *
     * This is commonly sent at the start of the information packet.
     */
    public static final char CC_FILE_INFO_START = (char) 28;
    /**
     * The character that marks the start of a file. (FS/File Separator)
     *
     * This is commonly sent at the start of the information packet.
     */
    public static final char CC_FILE_INFO_END = (char) 29;
    /**
     * The character that marks the end of a file. (EM/End of Medium)
     *
     * This is commonly sent after a file has been sent.
     */
    public static final char CC_FILE_END = (char) 25;
    /**
     * The character that starts escaping control characters. ($/Dollar Sign)
     */
    public static final char CC_CONTROL_ESCAPE = '$';

    public static final Map<Character, String> CONTROL_CHARACTER_ESCAPE_VALUES = new LinkedHashMap<Character, String>() {{
        put(CC_CONTROL_ESCAPE, "CE");
        put(CC_FILE_INFO_START, "FIS");
        put(CC_FILE_INFO_END, "FIE");
        put(CC_FILE_END, "FE");
        put(CC_PACKET_DELIMITER, "PD");
        put(CC_PACKET_START, "PS");
        put(CC_PACKET_END, "PE");
        put(CC_TRANSMISSION_END, "TE");
    }};

    public enum TransmissionStage {
        Idle,
        FileID,
        FileName,
        FileHash,
        FileLength,
        PacketIdle,
        PacketLocation,
        PacketLength,
        PacketData,
        Disconnected
    }

}
