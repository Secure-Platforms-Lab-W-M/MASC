package masc.edu.wm.cs.masc.barebone.mutationmakers;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import masc.edu.wm.cs.masc.builders.generic.BuilderMainClass;
import masc.edu.wm.cs.masc.builders.generic.BuilderMainMethod;
import masc.edu.wm.cs.masc.operator.OperatorType;
import masc.edu.wm.cs.masc.operator.restrictive.intoperator.Arithmetic;
import masc.edu.wm.cs.masc.operator.restrictive.intoperator.IterationMultipleCall;
import masc.edu.wm.cs.masc.operator.restrictive.intoperator.ValueInVariable;
import masc.edu.wm.cs.masc.operator.restrictive.intoperator.ValueInVariableArithmetic;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

public class IntMutationMaker extends AMutationMaker{

    IntOperatorProperties p;

    public IntMutationMaker(IntOperatorProperties p) {
        this.p = p;
    }

    @Override
    public void populateOperators() {
        operators.put(OperatorType.IntValueInVariable, new ValueInVariable(p));
        operators.put(OperatorType.IntArithmetic, new Arithmetic(p));
        operators.put(OperatorType.IntValueInVariableArithmetic, new ValueInVariableArithmetic(p));
        operators.put(OperatorType.IntIterationMultipleCall, new IterationMultipleCall(p));
    }

    @Override
    public String getContent(OperatorType operatorType) {
        TypeSpec.Builder builder = BuilderMainClass
                .getClassBody(p.getClassName());
        System.out.println("Processing: " + operatorType.name());
        MethodSpec.Builder mainMethod = BuilderMainMethod
                .getMethodSpecWithException();
        mainMethod.addCode(operators.get(operatorType).mutation());
        builder.addMethod(mainMethod.build());
        return builder.build().toString();
    }
}
