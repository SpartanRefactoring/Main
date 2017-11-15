package metatester.aux_layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Oren Afek
 * @since 04-Jul-17.
 */
public class CollectionsUtils {

    public static <T> List<T> merge(List<T> l1, List<T> l2) {
        List<T> l = new ArrayList<>();
        l.addAll(l1);
        l.addAll(l2);
        return l;
    }

    public static <T> List<T> copyOf(final List<T> ¢) {
        final List<T> copy = new ArrayList<>(¢);
        Collections.copy(copy, ¢);
        return copy;
    }

    public static <T> List<T> reverse(final List<T> ¢) {
        Collections.reverse(¢);
        return ¢;
    }

}
