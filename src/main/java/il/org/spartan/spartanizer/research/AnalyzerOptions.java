package il.org.spartan.spartanizer.research;

import java.util.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.utils.*;

/** Created out of {@link ConfigurableObjectTemplate}
 * @author Ori Marcovitch
 * @since Nov 1, 2016 */
public interface AnalyzerOptions {
  Map<String, Map<String, String>> options = new HashMap<>();
  Bool verbose = new Bool();

  static String get(final String cls, final String property) {
    return options.get(cls) == null ? null : options.get(cls).get(property);
  }

  static void set(final String cls, final String property, final String value) {
    if (options.get(cls) == null)
      options.put(cls, new HashMap<>());
    options.get(cls).put(property, value);
  }

  static String get(final String property) {
    return get(Analyzer.class.getSimpleName(), property);
  }

  static void set(final String property, final String value) {
    set(Analyzer.class.getSimpleName(), property, value);
  }

  Int counter = new Int();
  String INPUT_DIR = "inputDir";

  static void tickNP() {
    if (!verbose.inner)
      return;
    ++counter.inner;
    if (counter.inner == 90) {
      counter.inner = 0;
      System.out.println("");
    }
    System.out.print(".");
  }

  static void setVerbose() {
    verbose.inner = true;
  }
}