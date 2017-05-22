package il.org.spartan.Leonidas.plugin.leonidas;

import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Method;

import java.util.function.Supplier;

/**
 * @author Oren Afek
 * @since 29-03-2017.
 */
public abstract class The {
    static The the;

    public Method asMethod;

    public static The element(int id) {
        return the;
    }

    public static The the(Object... objects) {
        return the;
    }

    public abstract EndThe is(Runnable template);

    public abstract EndThe is(Supplier<?> template);

    public abstract EndThe isNot(Runnable template);

    public abstract EndThe isNot(Supplier<?> template);

    class EndThe {
        public <T> void ofType(Class<? extends T> __) {/**/}
    }
}
