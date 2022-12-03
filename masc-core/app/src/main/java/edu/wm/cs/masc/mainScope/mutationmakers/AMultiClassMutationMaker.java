package edu.wm.cs.masc.mainScope.mutationmakers;

import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.mutation.properties.AOperatorProperties;
import edu.wm.cs.masc.utils.file.CustomFileWriter;
import edu.wm.cs.masc.utils.file.FilePack;

import java.io.File;
import java.util.ArrayList;
import edu.wm.cs.masc.logger.LocalLogger;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Is abstract mutation maker that involves creating instances of mutation
 * for bare-bone format involving multiple classes for bare-bone layer of MASC
 */
public abstract class AMultiClassMutationMaker extends AMutationMaker {

    ArrayList<FilePack> filePacks;
    private static Logger logger = LogManager.getLogger(AMutationMaker.class);

    public void setFilepacks(ArrayList<FilePack> filePacks) {
        this.filePacks = filePacks;
    }

    @Override
    public void make(AOperatorProperties p) {
        populateOperators();
        for (OperatorType operatorType : operators.keySet()) {
            String content = getContent(operatorType, p);
            writeOutput(p.getOutputDir(), operatorType,
                    p.getClassName() + ".java",
                    content);
            writeDependencyClasses(operatorType);
        }
    }

    public void writeDependencyClasses(OperatorType operatorType) {
        if (filePacks == null || filePacks.size() < 1) {
            throw new IllegalArgumentException(
                    "Need at least one dependency class. Set FilePacks");
        }
        for (FilePack filepack : filePacks) {
            writeOutput(filepack.getPath(), operatorType,
                    filepack.getFilename() + ".java",
                    filepack.getContent());
        }
    }

    public void writeOutput(String path, OperatorType type, String fileName,
                            String content) {
        String dir_path = path + File.separator + type.name() + File.separator;
        if (!CustomFileWriter.WriteFile(dir_path, fileName, content)) {
            logger.trace("Something went wrong, check stack trace");
        } else {
            // Special logger for front end parsing
            LocalLogger.getLocalLogger().info("[OutputPath]#"+path+"/"+type.name()+"/"+fileName);
        }
    }

}
