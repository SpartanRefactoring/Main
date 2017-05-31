package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import java.util.HashMap;
import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyNumberOf;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;
import static il.org.spartan.Leonidas.plugin.leonidas.The.element;

/**
 * FluentSetter
 *
 * @author michalcohen, amirsagiv83
 * @since 29/5/17
 */
public class FluentSetter implements LeonidasTipperDefinition {

    @Override
    public void constraints() {
        element(1).asMethod.startsWith("set");
        element(4).isNot(() -> {
            return;
        });
    }

    @Override
    public void matcher() {
        new Template(() -> {
            /** start */
            class Class0 {
                void method1(Class2 identifier3) {
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
                Class0 method1(Class2 identifier3) {
                    anyNumberOf(statement(4));
                    return this;
                }
            }
            /** end */
        });
    }


    @Override
    public Map<String, String> getExamples() {
        Map<String, String> examples = new HashMap<>();
        // <enter examples>
        return examples;
    }

    class Class2 {
    }
}