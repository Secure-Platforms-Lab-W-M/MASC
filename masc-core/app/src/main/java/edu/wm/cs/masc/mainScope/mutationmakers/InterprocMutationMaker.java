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
        String otherClass1 = p.getOtherClassName() + "1";
        String otherClass2 = p.getOtherClassName() + "2";
        String otherClass3 = p.getOtherClassName() + "3";
        FilePack filePack = new FilePack(otherClass1, p.getOutputDir(),
                BuilderInterprocClass.getInterprocClassString(p));
        ArrayList<FilePack> filePacks = new ArrayList<>();
        filePacks.add(filePack);
        filePack = new FilePack(otherClass2, p.getOutputDir(),
                BuilderInterprocAdditionClass.getInterprocClassString(p));
        filePacks.add(filePack);
        filePack = new FilePack(otherClass3, p.getOutputDir(),
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
