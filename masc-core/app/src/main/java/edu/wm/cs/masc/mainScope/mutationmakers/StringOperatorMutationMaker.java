package edu.wm.cs.masc.mainScope.mutationmakers;

//import masc.edu.wm.cs.mascDeprecated.operator.restrictive.stringoperator.*;
import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;
import edu.wm.cs.masc.mutation.suppliers.MutationSupplier;

public class StringOperatorMutationMaker extends AMutationMaker {

    StringOperatorProperties p;

    @Override
    public void populateOperators() {
//        operators.put(OperatorType.StringDifferentCase, new DifferentCase(p));
//        operators.put(OperatorType.StringNoiseReplace, new NoiseReplace(p));
//        operators.put(OperatorType.StringSafeReplaceWithUnsafe,
//                new SafeReplaceWithUnsafe(p));
//        operators.put(OperatorType.StringUnsafeReplaceWithUnsafe,
//                new UnsafeReplaceWithUnsafe(p));
//        operators.put(OperatorType.StringStringCaseTransform,
//                new StringCaseTransform(p));
//        operators.put(OperatorType.StringValueInVariable,
//                new ValueInVariable(p));
        operators = new MutationSupplier(p).getOperators(p.getExcludedOperators());
    }

    public StringOperatorMutationMaker(StringOperatorProperties p) {
        this.p = p;
    }

}
