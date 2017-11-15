package metatester.aux_layer.mutables;

/**
 * @author Oren Afek
 * @since 17-Jun-17.
 */
public class Bool extends Mutable<Boolean>{

    public static Bool valueOf(final boolean ¢) {
        return new Bool(¢);
    }

    public boolean inner;

    public Bool() {
        super(false);
    }
    public Bool(final boolean b) {
        super(b);
    }
    public Bool clear() {
        return set(false);
    }

    /** Function form, good substitute for auto-boxing */
    public Boolean inner() {
        return Boolean.valueOf(inner);
    }
    public Bool set() {
        return set(true);
    }
    public Bool set(final boolean ¢) {
        inner = ¢;
        return this;
    }
}
