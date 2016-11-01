package il.org.spartan.spartanizer.research;

import java.util.*;

/** Created out of {@link ConfigurableObjectTemplate}
 * @author Ori Marcovitch
 * @since Nov 1, 2016 */
public interface AnalyzerOptions {
  Map<String, Map<String, String>> options = new HashMap<>();

  static String get(String cls, String property) {
    return options.get(cls) == null ? null : options.get(cls).get(property);
  }

  static void set(String cls, String property, String value) {
    if (options.get(cls) == null)
      options.put(cls, new HashMap<>());
    options.get(cls).put(property, value);
  }
}