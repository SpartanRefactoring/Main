package metatester.aux_layer;

/**
 * A simple POJO for keepeing two values together.
 * @author Oren Afek
 * @since 17-Jun-17.
 */
public class Pair<F, S> {

    public F first;
    public S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)", first, second);
    }

}
