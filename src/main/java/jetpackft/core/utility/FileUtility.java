package jetpackft.core.utility;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class FileUtility {

    /**
     * Sterilize a file name and replace all illegal characters. Also removes Windows-reserved filenames and Windows-specific rules.
     * @param fileName The filename to sterilize.
     * @param _replacement The character used to replace illegal characters.
     * @return The sterilized filename.
     * @throws IllegalArgumentException If the sterilized name is still invalid, or if the sterilized name is null or empty.
     */
    public static String sterilizeFileName(String fileName, char _replacement) throws IllegalArgumentException {
        String replacement = String.valueOf(_replacement);
        Pattern illegal = Pattern.compile("[/?<>\\\\:*|\"]");
        Pattern control = Pattern.compile("[\\x00-\\x1f\\x80-\\x9f]");
        Pattern reserved = Pattern.compile("^\\.+$");
        Pattern windowsReserved = Pattern.compile("^(con|prn|aux|nul|com[0-9]|lpt[0-9])(\\..*)?$",
                Pattern.CASE_INSENSITIVE);
        Pattern windowsTrailing = Pattern.compile("[.]+$");

        String newFileName = fileName;
        newFileName = illegal.matcher(newFileName).replaceAll(replacement);
        newFileName = control.matcher(newFileName).replaceAll(replacement);
        newFileName = reserved.matcher(newFileName).replaceAll(replacement);
        newFileName = windowsReserved.matcher(newFileName).replaceAll("");
        newFileName = windowsTrailing.matcher(newFileName).replaceAll(replacement);

        if (newFileName.equals("")) {
            throw new IllegalArgumentException("The sterilized file name is null or empty");
        } else if (newFileName.length() < 4) {
            throw new IllegalArgumentException("The sterilized file name is still invalid.");
        }

        try {
            File tempFile = File.createTempFile(newFileName, null);
            if (!tempFile.delete())
                throw new IllegalArgumentException("The sterilized file name is still invalid.");
        } catch (IOException e) {
            throw new IllegalArgumentException("The sterilized file name is still invalid.");
        }

        return newFileName;
    }

}
