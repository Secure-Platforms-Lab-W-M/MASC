package edu.wm.cs.masc.exhaustive.dataleak.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.commons.io.FileUtils;

/**
 * File utility contains different utility methods used to read and
 * manipulate files and directories
 *
 * @author Amit Seal Ami
 */
public class FileUtility {
    private static Logger logger = LogManager.getLogger(FileUtility.class);

    /**
     * reads a text file and returns the contents through StringBuffer
     *
     * @param filePath of the file
     * @return StringBuffer that contains the text contents of file
     * @throws FileNotFoundException filepath could not be resolved
     * @throws IOException           IO failed
     */
    public static StringBuffer readSourceFile(String filePath)
            throws FileNotFoundException, IOException {
        StringBuffer source = new StringBuffer();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = null;

        while ((line = reader.readLine()) != null) {
            source.append(line).append("\n");
        }
        reader.close();
        return source;
    }

    /**
     * sets up the directory where the mutated source codes will be kept
     */
	public static void setupMutantsDirectory() {
//    public static void setupMutantsDirectory() {

        try {
            String newRoot = Arguments.getMutantsFolder() +
//					File.separator +
//					operatorType +
                    File.separator + Arguments.getAppName();
            if (new File(newRoot).exists()) {
                FileUtils.deleteDirectory(new File(newRoot));
            }
            logger.trace(Arguments.getRootPath());
            FileUtils.copyDirectory(new File(Arguments.getRootPath()),
                    new File(newRoot));
            //why was this done? no idea.
            Arguments.setRootPath(newRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getMutationRootPath(String mutation) {
        return Arguments.getMutantsFolder() + File.separator + mutation + File.separator + Arguments.getAppName();
    }

    public static boolean testFileEquality(File expected, File actual) {
        try {
            BufferedReader br_expected = new BufferedReader(
                    new FileReader(expected));
            BufferedReader br_actual = new BufferedReader(
                    new FileReader(actual));
            String line_expected, line_actual;

            while ((line_expected = br_expected.readLine()) != null &&
                    (line_actual = br_actual.readLine()) != null) {

                line_expected = line_expected.trim();
                line_actual = line_actual.trim();

                // check line equality
                if (!line_actual.equals(line_expected)) {
                    logger.trace("line_expected: " + line_expected);
                    logger.trace("line_actual: " + line_actual);
                    return false;
                }
            }
            // make sure files are of the same size
            if (br_expected.readLine() != null || br_expected.readLine() != null) {
                logger.trace("files are of unequal size");
                return false;
            }

            br_expected.close();
            br_actual.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        // files are the same
        return true;
    }
}
