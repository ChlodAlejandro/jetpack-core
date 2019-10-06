package jetpackft.server.tests;

import jetpackft.core.utility.FileUtility;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class RegexTest {

    @Test
    public void fileNameReplacementTest() {
        String[] expected = new String[] {
            "fileName.txt",
            "file_Name.txt",
            "file___Name.txt",
            "file_",
            "file_Name_"
        };

        String[] actual = new String[] {
                FileUtility.sterilizeFileName("fileName.txt", '_'),
                FileUtility.sterilizeFileName("file:Name.txt", '_'),
                FileUtility.sterilizeFileName("file/_/Name.txt", '_'),
                FileUtility.sterilizeFileName("file..", '_'),
                FileUtility.sterilizeFileName("file\u0000Name.", '_')
        };

        assertArrayEquals(expected, actual);
    }

}
