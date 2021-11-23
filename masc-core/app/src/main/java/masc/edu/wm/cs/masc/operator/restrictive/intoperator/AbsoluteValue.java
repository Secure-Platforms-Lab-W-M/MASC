package masc.edu.wm.cs.masc.operator.restrictive.intoperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

public class AbsoluteValue extends AIntOperator {

    public AbsoluteValue(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        String s = "Math.abs(" + iterationCount + ")";
        return MisuseType.getTemplate(this, s);
    }
}