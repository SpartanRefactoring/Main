package il.org.spartan.Leonidas.auxilary_layer;

import java.util.HashMap;
import java.util.Map;

/**
 * Fluent map for specifying examples for Leonidas Tippers.
 *
 * @author Oren Afek
 * @since 30/5/2017
 */
public class ExampleMapFactory {

    private Map<String, String> map;

    public ExampleMapFactory() {
        map = new HashMap<>();
    }

    public ExampleMapFactory put(String key, String value) {
        map.put(key, value);
        return this;
    }

    public Map<String, String> map() {
        return map;
    }
}
