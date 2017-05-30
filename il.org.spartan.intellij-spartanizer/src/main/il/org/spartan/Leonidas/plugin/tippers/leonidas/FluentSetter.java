package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import java.util.HashMap;
import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyNumberOf;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;

/**
 * <Tipper description>
 *
 * @author michalcohen, amirsagiv83
 * @since 29-05-2017
 */
public class FluentSetter implements LeonidasTipperDefinition {

    /**
     * Write here additional constraints on the matcher tree.
     * The structural constraint are of the form:
     * element(<id>).{is/isNot}(() - > <template>)[.ofType(Psi class)];
     * There are non structural that you can apply, for example
     * element(<id>).asMethod.startsWith("set);
     */
    @Override
    public void constraints() {
        element(1).asMethod.startsWith("set");
        element(4).isNot(() -> { return; });
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /** start */
            class Class0 {
                void method1(Type2 identifier3) {
                    anyNumberOf(statement(4));
                }
            }
            /** end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /** start */
            class Class0 {
                Class0 method1(Type2 identifier3) {
                    anyNumberOf(statement(4));
                    return this;
                }
            }
            /** end */
        });
    }

    /**
     * Defines code examples and results after applying the tipper.
     * This is used to test the tipper.
     * example:
     * examples.put("!(!(x > 4))", "x > 4");
     */
    @Override
    public Map<String, String> getExamples() {
        Map<String, String> examples = new HashMap<>();
        // <enter examples>
        return examples;
    }

    class Type2 {
    }
}