package metatester.aux_layer.mutables;

/**
 * @author Oren Afek
 * @since 17-Jun-17.
 */
public class Int extends Mutable<Integer> {

    public Int() {
        super();
        this.inner = 0;
    }

    public Int(int initValue) {
        super(initValue);
    }

    public int next() {
        return this.inner++;
    }

    public int step() {
        return ++this.inner;
    }

    public static Int valueOf(int ¢) {
        return new Int(¢);
    }

    public void add(final int value) {
        inner += value;
    }
    public void add(final Int other) {
        inner += other.get();
    }
    public void clear() {
        inner = 0;
    }
}
