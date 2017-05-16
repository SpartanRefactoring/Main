package il.org.spartan.Leonidas.auxilary_layer;

/**
 * @author Sharon Kuninin, michalcohen
 * @since 26-03-2017
 */
public class Pair<T, K> {

    public T first;
    public K second;

    public Pair() {
        this(null, null);
    }

    public Pair(T first, K second) {
        this.first = first;
        this.second = second;
    }
}
