package edu.wm.cs.masc.mainScope.mutationmakers;

import edu.wm.cs.masc.mutation.builders.restrictive.BuilderInterprocAdditionClass;
import edu.wm.cs.masc.mutation.builders.restrictive.BuilderInterprocClass;
import edu.wm.cs.masc.mutation.builders.restrictive.BuilderInterprocConditionalClass;
import edu.wm.cs.masc.mutation.builders.restrictive.BuilderNestedConditionalIterations;
import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.mutation.properties.AOperatorProperties;
import edu.wm.cs.masc.mutation.properties.InterprocProperties;
import edu.wm.cs.masc.mutation.suppliers.MutationSupplier;
import edu.wm.cs.masc.utils.file.FilePack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class InterprocMutationMaker extends AMultiClassMutationMaker {
    private static Logger logger = LogManager.getLogger(AMutationMaker.class);

    InterprocProperties p;

    public InterprocMutationMaker(InterprocProperties p) {
        this.p = p;
        String otherClass = p.getOtherClassName();
        //String otherClass2 = p.getOtherClassName();
        //String otherClass3 = p.getOtherClassName();
        //String otherClass4 = p.getOtherClassName();
        FilePack filePack = new FilePack(otherClass, p.getOutputDir() + "/Interproc",
                BuilderInterprocClass.getInterprocClassString(p));
        ArrayList<FilePack> filePacks = new ArrayList<>();
        filePacks.add(filePack);
        //filePack = new FilePack(otherClass, p.getOutputDir() + "/InterprocAddition",
        //        BuilderInterprocAdditionClass.getInterprocClassString(p));
        //filePacks.add(filePack);
        /*filePack = new FilePack(otherClass, p.getOutputDir(),
                BuilderInterprocConditionalClass.getInterprocClassString(p));
        filePacks.add(filePack);
        filePack = new FilePack(otherClass, p.getOutputDir(),
                BuilderNestedConditionalIterations.getInterprocClassString(p));
        filePacks.add(filePack);*/

        this.setFilepacks(filePacks);
    }


    @Override
    public void populateOperators() {

//        operators.put(OperatorType.Interproc, new InterProcOperator(p));
        operators = new MutationSupplier(p).getOperators(p.getExcludedOperators());
    }


    /*@Override
    public void make(AOperatorProperties p) {
        populateOperators();
        for (OperatorType operatorType : operators.keySet()) {
            String content = getContent(operatorType, p);
            logger.trace("make");
            writeOutput(p.getOutputDir(), operatorType,
                    p.getClassName() + ".java",
                    content.replaceAll("%d", ""));
        }
    }*/

}
