package metatester.aux_layer.mutables;

/**
 * @author Oren Afek
 * @since 17-Jun-17.
 */
abstract class Mutable<T> {

    public T inner;

    public T get() {
        return this.inner;
    }

    public Mutable() {

    }

    public Mutable(T initValue) {
        this.inner = initValue;
    }

    public Mutable<T> set(T value) {
        this.inner = value;
        return this;
    }

    @Override
    public String toString() {
        return inner.toString();
    }


}
