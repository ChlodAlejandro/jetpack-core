package jetpackft.server.tests;


import jetpackft.core.utility.PacketUtility;
import org.junit.Test;

import java.util.Map;

import static jetpackft.core.reference.TransmissionConstants.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class EncodingTest {

    @Test
    public void regexTest() {
        assertEquals("TestSentence", "TestSen$LT$ence".replaceAll("\\$LT\\$", "t"));
    }

    @Test
    public void decodeTest() {
        byte[] expected = new String(new byte[] {
                CC_FILE_INFO_START,
                CC_FILE_INFO_END,
                CC_CONTROL_ESCAPE,
                CC_FILE_END,
                CC_PACKET_DELIMITER,
                CC_PACKET_START,
                CC_PACKET_END,
                CC_TRANSMISSION_END
        }).getBytes();
        byte[] decoded = PacketUtility.decodePackets("$FIS$FIE$CE$FE$PD$PS$PE$TE".getBytes());

        System.out.printf("Expected:\t%s\n", new String(expected));
        System.out.printf("Encoded:\t%s\n", new String(decoded));

        assertArrayEquals(expected, decoded);
    }

    @Test
    public void encodeTest() {
        byte[] expected = "$CE$CE$FIS$FIE$CE$FE$PD$PS$PE$TE".getBytes();
        byte[] encoded = PacketUtility.encodePackets(new String(new byte[] {
                (byte) '$',
                (byte) '$',
                CC_FILE_INFO_START,
                CC_FILE_INFO_END,
                CC_CONTROL_ESCAPE,
                CC_FILE_END,
                CC_PACKET_DELIMITER,
                CC_PACKET_START,
                CC_PACKET_END,
                CC_TRANSMISSION_END
        }).getBytes());

        System.out.printf("Expected:\t%s\n", new String(expected));
        System.out.printf("Encoded:\t%s\n", new String(encoded));

        assertArrayEquals(expected, encoded);
    }

    @Test
    public void complicatedEncodeAndDecodeTest() {
        byte[] expected = new byte[] {
                CC_PACKET_START,
                (byte) 'T',
                (byte) 'E',
                CC_PACKET_END,
                CC_CONTROL_ESCAPE,
                (byte) 'P',
                (byte) 'S'
        };
        byte[] encoded = PacketUtility.encodePackets(expected);
        byte[] decoded = PacketUtility.decodePackets(encoded);


        System.out.printf("Expected:\t%s\n", new String(expected));
        System.out.printf("Encoded:\t%s\n", new String(encoded));
        System.out.printf("Decoded:\t%s\n", new String(decoded));

        assertArrayEquals(expected, decoded);
    }

    @Test
    public void byteReplaceTest() {
        assertArrayEquals(new byte[] {1, 7, 8, 9, 0, 4, 3, 2, 1},
                PacketUtility.replaceInByteArray(
                        new byte[] {1, 2, 3, 4, 3, 2, 1},
                        new byte[] {2, 3},
                        new byte[] {7, 8, 9, 0}));
    }

    @Test
    public void getBytesOfTransmissionConstants() {
        for (Map.Entry<Character, String> entry : CONTROL_CHARACTER_ESCAPE_VALUES.entrySet()) {
            System.out.println(entry.getValue() + " : " + PacketUtility.bytesToByteString(entry.getValue().getBytes()));
            System.out.println(entry.getKey() + " : " + String.valueOf((byte) ((char) entry.getKey())));
        }
    }

}
