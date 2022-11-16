package edu.wm.cs.masc.mainScope.mutationmakers;

import edu.wm.cs.masc.mutation.builders.restrictive.BuilderInterprocClass;
import edu.wm.cs.masc.mutation.properties.InterprocProperties;
import edu.wm.cs.masc.mutation.suppliers.MutationSupplier;
import edu.wm.cs.masc.utils.file.FilePack;

import java.util.ArrayList;

public class InterprocMutationMaker extends AMultiClassMutationMaker {
    InterprocProperties p;

    public InterprocMutationMaker(InterprocProperties p) {
        this.p = p;
        String otherClass = p.getOtherClassName();
        FilePack filePack = new FilePack(otherClass, p.getOutputDir(),
                BuilderInterprocClass.getInterprocClassString(p));
        ArrayList<FilePack> filePacks = new ArrayList<>();
        filePacks.add(filePack);
        this.setFilepacks(filePacks);
    }


    @Override
    public void populateOperators() {
//        operators.put(OperatorType.Interproc, new InterProcOperator(p));
        operators = new MutationSupplier(p).getOperators(p.getExcludedOperators());
    }

}
