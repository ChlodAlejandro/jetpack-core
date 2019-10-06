package jetpackft.core.utility;

import java.util.List;

public class ListUtility {

    public static byte[] convertByteListToArray(List<Byte> list) {
        byte[] array = new byte[list.size()];

        for (int x = 0; x < list.size(); x++) {
            array[x] = list.get(x);
        }

        return array;
    }

    public static char[] convertCharListToArray(List<Character> list) {
        char[] array = new char[list.size()];

        for (int x = 0; x < list.size(); x++) {
            array[x] = list.get(x);
        }

        return array;
    }

}
