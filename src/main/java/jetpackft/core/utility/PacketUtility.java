package jetpackft.core.utility;

import jetpackft.core.reference.TransmissionConstants;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;

public class PacketUtility {

    /**
     * Encode packets using an escape matrix.
     * @param packetData The byte array of packet data.
     * @return The encoded packets. This can now be sent through a stream without breaking controls.
     */
    public static byte[] encodePackets(byte[] packetData) {
        return encodePackets(packetData, TransmissionConstants.CONTROL_CHARACTER_ESCAPE_VALUES);
    }

    /**
     * Encode packets using an escape matrix.
     * @param _packetData The byte array of packet data.
     * @param escapeMatrix The escape matrix. A Map containing the characters to escape and their escape values.
     * @return The encoded packets. This can now be sent through a stream without breaking controls.
     */
    public static byte[] encodePackets(byte[] _packetData, Map<Character, String> escapeMatrix) {
        byte[] packetData = _packetData;
        for (Map.Entry<Character, String> escapeValue : escapeMatrix.entrySet()) {
            packetData = replaceInByteArray(packetData,
                    new byte[] { (byte) ((char) escapeValue.getKey()) },
                    (TransmissionConstants.CC_CONTROL_ESCAPE +
                        escapeValue.getValue()).getBytes());
        }
        return packetData;
    }

    /**
     * Decode packet data using an escape matrix.
     * @param packetData The packet data to decode.
     * @return The decoded packet data.
     */
    public static byte[] decodePackets(byte[] packetData) {
        return decodePackets(packetData,
                MapUtility.reverseKeys(TransmissionConstants.CONTROL_CHARACTER_ESCAPE_VALUES));
    }

    /**
     * Decode packet data using an escape matrix.
     * @param _packetData The packet data to decode.
     * @param escapeMatrix The escape matrix to use.
     * @return The decoded packet data.
     */
    public static byte[] decodePackets(byte[] _packetData, Map<String, Character> escapeMatrix) {
        byte[] packetData = _packetData;

        ListIterator<Map.Entry<String, Character>> iter =
                new ArrayList<>(escapeMatrix.entrySet()).listIterator(escapeMatrix.size());

        while (iter.hasPrevious()) {
            Map.Entry<String, Character> escapeValue = iter.previous();
            packetData = replaceInByteArray(packetData,
                    String.format("%s%s",
                            TransmissionConstants.CC_CONTROL_ESCAPE,
                            escapeValue.getKey()).getBytes(),
                    new byte[] { (byte) ((char) escapeValue.getValue())});
        }

        return packetData;
    }

    /**
     * Replace a subarray with another array.
     *
     * <p>For the given array <code>[1, 2, 3, 4]</code>, the given search sequence <code>[2, 3]</code> and the given replacement sequence <code>[5, 6]</code>, the expected final result will be <code>[1, 5, 6, 4]</code></p>
     * @param _byteSequence The byte array to look from.
     * @param searchSequence The sequence to search in the byte sequence.
     * @param replacementSequence The sequence to replace the search sequence with.
     * @return An array with all matching search patterns replaced with the replacement sequence.
     */
    public static byte[] replaceInByteArray(byte[] _byteSequence, byte[] searchSequence, byte[] replacementSequence) {
        byte[] byteSequence = _byteSequence;
        finderLoop: for (int bi = 0; bi < byteSequence.length; bi++) {
            if (byteSequence[bi] == searchSequence[0] && bi + (searchSequence.length - 1) < byteSequence.length) {
                for (int ssi = 0; ssi < searchSequence.length; ssi++) {
                    if (byteSequence[bi + ssi] != searchSequence[ssi])
                        continue finderLoop;
                    else if (ssi == searchSequence.length - 1) {
                        for (int rsi = 0; rsi < searchSequence.length; rsi++) {
                            byteSequence = ArrayUtils.remove(byteSequence, bi);
                        }
                        int isi = 0;
                        for (; isi < replacementSequence.length; isi++) {
                            byteSequence = ArrayUtils.insert(bi + isi, byteSequence, replacementSequence[isi]);
                        }
                        bi += isi - 1;
                    }
                }
            }
        }
        return byteSequence;
    }

    public static String bytesToByteString(byte[] bytes) {
        StringBuilder outputString = new StringBuilder();

        for (byte b : bytes) {
            outputString.append(String.valueOf(b)).append(" ");
        }

        return outputString.toString();
    }

}
