package edu.wm.cs.masc.mutation.operators.restrictive.stringoperator;

import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;

    public class InjectInsecureObject extends AStringOperator {

        public InjectInsecureObject(StringOperatorProperties properties) {
            super(properties);
        }

        @Override
        public String mutation() {
            StringBuilder s = new StringBuilder();
            s.append("insecureObjectName")
                    .append(" ")
                    .append("foo")
                    .append(" ")
                    .append("=")
                    .append(" ")
                    .append("insecureObjectName")
                    .append("()")
                    .append(";");
            return s.toString();
        }
    }


