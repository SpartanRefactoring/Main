package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import il.org.spartan.Leonidas.plugin.leonidas.Matcher;

import java.util.List;
import java.util.Map;

/**
 * @author Oren Afek
 * @since 30-Jun-17.
 */
public class Identifier extends Identifiable {

    private static final String TEMPLATE = "identifier";

    // DO NOT REMOVE, REFLECTION PURPOSES!
    public Identifier() {
        super(TEMPLATE);
    }

    public Identifier(Encapsulator e) {
        super(e, TEMPLATE);
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> m) {
        return new Identifier(e);
    }
}
