package jetpackft.server.tests.utility;

import jetpackft.core.io.Log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;

import static org.junit.Assert.fail;

public class TestUtility {

    public static File getTestResourceFile(Log log, String name) {
        ClassLoader classLoader = TestUtility.class.getClassLoader();
        URL file = classLoader.getResource("testFiles/" + name);

        if (file == null) {
            log.appendToLog(Level.SEVERE, "TestFileLoader", "File is null (" + name + ").");
            fail();
        }

        String filePath = null;
        try {
            filePath = URLDecoder.decode(file.getFile(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.appendToLog(Level.SEVERE, "TestFileLoader", "Unsupported encoding (" + name + ").");
            e.printStackTrace();
            fail();
        }

        log.appendToLog(Level.FINE, "TestFileLoader", "Loaded file: " + filePath);
        return new File(filePath);
    }

}
