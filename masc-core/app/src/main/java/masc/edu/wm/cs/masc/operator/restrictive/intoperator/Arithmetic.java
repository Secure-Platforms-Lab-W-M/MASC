package masc.edu.wm.cs.masc.operator.restrictive.intoperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;
import masc.edu.wm.cs.masc.utility.RandomGeneratorFactory;

import java.util.Random;

public class Arithmetic extends AIntOperator {

    public Arithmetic(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {

        // Get the iteration count as an integer
        int iterCount = Integer.parseInt(iterationCount);

        // Generate a random value between -iterCount and +iterCount
        Random gen = new RandomGeneratorFactory().getGenerator();
        int term1 = (int) (gen.nextDouble() * 2 * iterCount) - iterCount;
        int term2 = iterCount - term1;

        StringBuilder s = new StringBuilder();
        s.append(super.mutation());
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"").append(password).append("\", ")
                .append("salt").append(", ")
                .append(term1 + " + " + term2).append(")")
                .append(";");
        return s.toString();
    }
}
