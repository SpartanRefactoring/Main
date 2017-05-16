package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import java.util.function.Supplier;

/**
 * @author Sharon Kuninin, Oren Afek
 * @since 26-03-2017.
 */
public interface LeonidasTipperDefinition {
    void matcher();

    void replacer();

    default void constraints() {
    }

    class Template {
        public Template(Runnable __) {/**/}

        public Template(Supplier<?> __) {/**/}
    }
}
