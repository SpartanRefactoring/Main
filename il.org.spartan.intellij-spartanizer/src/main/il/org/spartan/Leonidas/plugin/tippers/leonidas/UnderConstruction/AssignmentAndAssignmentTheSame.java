package il.org.spartan.Leonidas.plugin.tippers.leonidas.UnderConstruction;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason.BROKEN_MATCHER;

/**
 * Inline assignment into subsequent assignment.
 *
 * @author Oren Afek
 * @since 29-06-2017
 */
@TipperUnderConstruction(BROKEN_MATCHER) //Waiting for AnyNumberOfParameter
public class AssignmentAndAssignmentTheSame implements LeonidasTipperDefinition {


    X identifier0;

    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            identifier0 = identifier0.methodInvocation1();
            identifier0 = identifier0.methodInvocation2();
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /* start */
            identifier0 = identifier0.methodInvocation1().methodInvocation2();
            /* end */
        });
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("s = s.trim();\n s = s.substring(0,1);", "s = s.trim().substring(0,1);")
                .map();

    }

    class X {

        public X methodInvocation1() {
            return this;
        }

        public X methodInvocation2() {
            return this;
        }
    }
}