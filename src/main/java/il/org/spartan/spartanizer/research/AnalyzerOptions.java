package il.org.spartan.spartanizer.research;

import java.util.*;

import il.org.spartan.plugin.*;

/** Created out of {@link ConfigurableObjectTemplate}
 * @author Ori Marcovitch
 * @since Nov 1, 2016 */
public interface AnalyzerOptions {
  Map<String, Map<String, String>> options = new HashMap<>();

  static String get(final String cls, final String property) {
    return options.get(cls) == null ? null : options.get(cls).get(property);
  }

  static void set(final String cls, final String property, final String value) {
    if (options.get(cls) == null)
      options.put(cls, new HashMap<>());
    options.get(cls).put(property, value);
  }
}