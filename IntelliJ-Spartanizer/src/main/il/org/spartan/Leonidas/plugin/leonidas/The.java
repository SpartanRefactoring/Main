package il.org.spartan.Leonidas.plugin.leonidas;

import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Method;

import java.util.function.Supplier;

/**
 * @author Oren Afek
 * @since 29-03-2017.
 */
public abstract class The {
    static The the;

    public Method method;

    public static The the(Object... objects) {
        return the;
    }

    abstract EndThe is(Runnable template);

    abstract EndThe is(Supplier<?> template);

    abstract EndThe isNot(Runnable template);

    abstract EndThe isNot(Supplier<?> template);

    class EndThe {
        public <T> void ofType(Class<? extends T> __) {/**/}
    }
}
