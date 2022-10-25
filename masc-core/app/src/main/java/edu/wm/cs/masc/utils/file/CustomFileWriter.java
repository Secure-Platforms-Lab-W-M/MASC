package edu.wm.cs.masc.utils.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class CustomFileWriter {
    private static Logger logger = LogManager.getLogger(CustomFileWriter.class);

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean WriteFile(String file_dir, String filename,
                                    String content) {
        File directory = new File(file_dir);

        if(directory.mkdirs()){
            logger.trace("Making necessary directories: "+file_dir);
        }
        File file = new File(directory, filename);
        try {
            if(file.createNewFile()){
                logger.trace("File does not exist, creating: "+ file.getAbsolutePath());
            }
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
