package edu.wm.cs.masc.mainScope.mutationmakers;

import edu.wm.cs.masc.mutation.builders.restrictive.BuilderInterprocAdditionClass;
import edu.wm.cs.masc.mutation.builders.restrictive.BuilderInterprocClass;
import edu.wm.cs.masc.mutation.builders.restrictive.BuilderInterprocConditionalClass;
import edu.wm.cs.masc.mutation.properties.InterprocProperties;
import edu.wm.cs.masc.mutation.suppliers.MutationSupplier;
import edu.wm.cs.masc.utils.file.FilePack;

import java.util.ArrayList;

public class InterprocMutationMaker extends AMultiClassMutationMaker {
    InterprocProperties p;

    public InterprocMutationMaker(InterprocProperties p) {
        this.p = p;
        String otherClass1 = p.getOtherClassName();
        String otherClass2 = p.getOtherClassName();
        String otherClass3 = p.getOtherClassName();
        FilePack filePack = new FilePack(otherClass1, p.getOutputDir()+"/InterprocOperator",
                BuilderInterprocClass.getInterprocClassString(p));
        ArrayList<FilePack> filePacks = new ArrayList<>();
        filePacks.add(filePack);
        filePack = new FilePack(otherClass2, p.getOutputDir()+"/InterprocAddition",
                BuilderInterprocAdditionClass.getInterprocClassString(p));
        filePacks.add(filePack);
        filePack = new FilePack(otherClass3, p.getOutputDir()+"/InterprocConditional",
                BuilderInterprocConditionalClass.getInterprocClassString(p));
        filePacks.add(filePack);

        this.setFilepacks(filePacks);
    }


    @Override
    public void populateOperators() {

//        operators.put(OperatorType.Interproc, new InterProcOperator(p));
        operators = new MutationSupplier(p).getOperators(p.getExcludedOperators());
    }

}
