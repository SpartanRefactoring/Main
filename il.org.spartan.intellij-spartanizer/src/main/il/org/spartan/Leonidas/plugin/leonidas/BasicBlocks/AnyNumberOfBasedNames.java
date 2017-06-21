package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

/**
 * Created by melanyc on 6/20/2017.
 */
public abstract class AnyNumberOfBasedNames extends QuantifierBasedNames {

    public AnyNumberOfBasedNames(Encapsulator e, String template, Encapsulator i) {
        super(e, template, i);
        internal = i;
    }

    public AnyNumberOfBasedNames(String template) {
        super(template);
    }
}
