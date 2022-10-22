package edu.wm.cs.masc.mainScope.mutationmakers;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import edu.wm.cs.masc.logger.LocalLogger;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainClass;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainMethod;
import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.mutation.properties.AOperatorProperties;
import edu.wm.cs.masc.utils.file.CustomFileWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Is the Abstract Mutation maker that
 * creates instances of mutations for bare-bone layer of MASC.
 * To simplify, it is given the body of mutation, classname and other information,
 * it outputs an instance of mutation in mainScope format.
 */

public abstract class AMutationMaker {
    private static Logger logger = LogManager.getLogger(AMutationMaker.class);

    public HashMap<OperatorType, IOperator> operators =
            new HashMap<>();

    public String getContent(OperatorType operatorType, AOperatorProperties p){
        TypeSpec.Builder builder = BuilderMainClass
                .getClassBody(p.getClassName());
        logger.trace("Processing: " + operatorType.name());
        MethodSpec.Builder mainMethod = BuilderMainMethod
                .getMethodSpecWithException();
        mainMethod.addCode(operators.get(operatorType).mutation());
        builder.addMethod(mainMethod.build());
        return builder.build().toString();
    }

    //if multiple types of operators fall under same category, and we want them all to be created.
    public abstract void populateOperators();

    public void make(AOperatorProperties p) {
        populateOperators();
        for (OperatorType operatorType : operators.keySet()) {
            String content = getContent(operatorType, p);
            writeOutput(p.getOutputDir(), operatorType,
                    p.getClassName() + ".java",
                    content.replaceAll("%d", ""));
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
