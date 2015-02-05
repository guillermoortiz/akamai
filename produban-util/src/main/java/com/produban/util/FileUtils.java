package com.produban.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Utilities for files.
 */
public final class FileUtils {

    /**
     * Instance of the log object.
     */
    private static final Logger LOG = Logger.getLogger(FileUtils.class);
    /**
     * Constant for buffer size.
     */
    public static final int BUFFER_SIZE = 1024;

    /**
     * Private constructor, only static methods.
     */
    private FileUtils() {
    }

    /**
     * Read an InputStrem and write directly in a outputStream saving memory.
     * 
     * @param is
     *            the instance of InputStream.
     * @param ou
     *            the instance of OutputStream.
     * @return boolean with the result
     */
    public static boolean writeBinaryFile(final InputStream is, final OutputStream ou) {

        BufferedInputStream reader = null;
        BufferedOutputStream writer = null;
        final byte[] buf = new byte[BUFFER_SIZE];
        int numRead = 0;

        boolean result = true;

        try {
            writer = new BufferedOutputStream(ou);
            reader = new BufferedInputStream(is);
            while ((numRead = is.read(buf)) != -1) {
                writer.write(buf, 0, numRead);
            }

        } catch (IOException ex) {
            LOG.error("Error maanging the files", ex);
            result = false;

        } finally {
            try {
                writer.close();
                reader.close();
            } catch (IOException ex) {
                LOG.error("Error closing the files", ex);
            }
        }

        return result;
    }

    /**
     * Read an InputStrem and return a string with maxLinesProcesed.
     * 
     * @param inputStream
     *            the instance of InputStream
     * @param maxLinesProcesed
     *            number max of lines to return
     * @return string with maxLinesProcesed lines
     * @throws IOException
     *             exception
     */
    public static String getLinesFile(final InputStream inputStream, final int maxLinesProcesed)
            throws IOException {

        String fileString = "";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        int contLine = 0;
        while ((line = bufferedReader.readLine()) != null) {
            if (contLine <= maxLinesProcesed) {
                fileString += line + "\n";
                contLine++;
            } else {
                break;
            }
        }
        return fileString;
    }

    /**
     * Read an InputStrem in XML and return a string with maxLinesProcesed.
     * 
     * @param inputStream
     *            the instance of InputStream
     * @param maxLinesProcesed
     *            number max of lines to return
     * @return string with maxLinesProcesed lines
     * @throws IOException
     *             exception
     */
    public static String getLinesXMLFile(final InputStream inputStream, final int maxLinesProcesed)
            throws IOException {

        StringBuilder output = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        boolean initial = false;

        String line = bufferedReader.readLine();
        int contLine = 0;
        String xmlLine = "";
        String etiqFinal = "";
        while (line != null && contLine <= maxLinesProcesed) {

            int position = StringUtils.indexOf(line, "<");
            String lastLine = "";
            if (initial) {
                position = 0;
            }
            while (position != -1) {
                if (!initial) {
                    char character = line.charAt(position + 1);
                    if (character != '?' && character != '!') {
                        int pSpace = StringUtils.indexOf(line, " ", position);
                        int pClose = StringUtils.indexOf(line, ">", position);
                        if ((pSpace > pClose || pSpace == -1) && pClose != -1) {
                            pSpace = pClose;
                        }
                        if (pSpace != -1 && pClose != -1) {
                            String etiq = StringUtils.substring(line, position + 1, pSpace);
                            etiqFinal = "</" + etiq + ">";
                            initial = true;
                        } else {
                            lastLine = StringUtils.substring(line, position);
                            position = -1;
                        }
                    } else {
                        line = StringUtils.substring(line, position + 1);
                        position = StringUtils.indexOf(line, "<");
                        initial = false;
                    }
                }
                if (initial) {
                    int positionFinal = StringUtils.indexOf(line, etiqFinal);
                    if (positionFinal != -1) {
                        initial = false;
                        xmlLine =
                                xmlLine
                                        + StringUtils.substring(line, position,
                                                StringUtils.indexOf(line, ">", positionFinal) + 1);
                        output.append(xmlLine);
                        output.append("\n");
                        contLine++;
                        line =
                                StringUtils.substring(line,
                                        StringUtils.indexOf(line, ">", positionFinal) + 1);
                        position = StringUtils.indexOf(line, "<");
                        xmlLine = "";
                        etiqFinal = "";
                    } else {
                        xmlLine = xmlLine + StringUtils.substring(line, position);
                        position = -1;
                    }

                }
            }
            line = bufferedReader.readLine();
            if (line != null) {
                line = lastLine + " " + line;
            }
        }
        return output.toString();
    }
}
