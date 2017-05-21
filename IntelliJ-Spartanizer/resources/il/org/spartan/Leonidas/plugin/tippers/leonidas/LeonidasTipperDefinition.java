package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import java.util.function.Supplier;
import java.util.Map;

/**
 * @author Sharon Kuninin, Oren Afek
 * @since 26-03-2017.
 */
public interface LeonidasTipperDefinition {
    /**
     * Defines the template to which the code of the user must initially match.
     */
    void matcher();

    /**
     * Defines the template to which the code of the user will be replaced.
     */
    void replacer();

    /**
     * Defines additional constraints that need to apply on the code of the user so it will be matched.
     */
    default void constraints() {
    }

    /**
     * Defined a generic template of code.
     */
    class Template {
        /**
         * Will be used when ever the method inside the template doesn't return anything.
         * For example: if(booleanExpression(0)) { statement(1); }
         *
         * @param __ When creating an instance for this param, the method "run" will be override to define the template.
         */
        public Template(Runnable __) {/**/}

        /**
         * Will be used when ever the method inside the template returns something.
         * For example: !booleanExpression(0) or if(booleanExpression(0) return expression(1);
         * @param __ When creating an instance for this param, the method "get" will be override to define the template.
         */
        public Template(Supplier<?> __) {/**/}
    }

    /*
    Defines code examples and results after applying the tipper. This is used to test the tipper.
     */
    default Map<String,String> getExamples() {return null;}
}
