package edu.wm.cs.masc.mainScope.mutationmakers;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainClass;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainMethod;
import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.mutation.operators.restrictive.byteoperator.CurrentTime;
import edu.wm.cs.masc.mutation.properties.AOperatorProperties;
import edu.wm.cs.masc.mutation.properties.ByteOperatorProperties;
import edu.wm.cs.masc.mutation.suppliers.MutationSupplier;

public class ByteMutationMaker extends AMutationMaker {

    ByteOperatorProperties p;

    public ByteMutationMaker(ByteOperatorProperties p) {
        this.p = p;
    }

    @Override
    public String getContent(OperatorType operatorType, AOperatorProperties p) {
        TypeSpec.Builder builder = BuilderMainClass
                .getClassBody(p.getClassName());
        System.out.println("Processing: " + operatorType.name());
        MethodSpec.Builder mainMethod = BuilderMainMethod
                .getMethodSpec();
        mainMethod.addCode(operators.get(operatorType).mutation());
        builder.addMethod(mainMethod.build());
        return builder.build().toString();
    }

    @Override
    public void populateOperators() {
//        operators.put(OperatorType.ByteCurrentTime, new CurrentTime(p));
//        operators.put(OperatorType.ByteLoop, new CurrentTime(p));
        operators = new MutationSupplier(p).getOperators();
    }
}
