package masc.edu.wm.cs.masc.operator.restrictive.intoperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

public class NestedClass extends AIntOperator {

    public NestedClass(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {

        // Create String Builder
        StringBuilder s = new StringBuilder();

        // Write the nested class
        s.append("class NestedClass{\n\t")
                .append("int getIteration(){\n\t\t")
                .append("return " + iterationCount)
                .append(";\n\t}\n}\n");

        // Get the misuse template
        s.append(MisuseType.getTemplate(this,
                "new NestedClass().getIteration()"));

        // Return the generated string
        return s.toString();
    }
}
