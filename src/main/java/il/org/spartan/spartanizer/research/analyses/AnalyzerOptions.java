package il.org.spartan.spartanizer.research.analyses;

import java.util.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.utils.*;

/** Created out of {@link ConfigurableObjectTemplate}
 * @author Ori Marcovitch
 * @since Nov 1, 2016 */
public enum AnalyzerOptions {
  ;
  static Map<String, Map<String, String>> options = new HashMap<>();
  static Bool verbose = new Bool();

  public static String get(final String cls, final String property) {
    return options.get(cls) == null ? null : options.get(cls).get(property);
  }

  static void set(final String cls, final String property, final String value) {
    if (options.get(cls) == null)
      options.put(cls, new HashMap<>());
    options.get(cls).put(property, value);
  }

  public static String get(final String property) {
    return get(Analyzer.class.getSimpleName(), property);
  }

  static void set(final String property, final String value) {
    set(Analyzer.class.getSimpleName(), property, value);
  }

  static Int counter = new Int();
  public static String INPUT_DIR = "inputDir";

  public static void tickNP() {
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

  public static void parseArguments(final String[] args) {
    if (args.length < 2)
      assert false : "You need to specify at least inputDir and outputDir!\nUsage: Analyzer -option=<value> -pattern.option2=<value> ...\n";
    for (final String arg : args)
      parseArgument(arg);
    System.out.println(AnalyzerOptions.options);
  }

  private static void parseArgument(final String s) {
    assert s.charAt(0) == '-' : "property should start with '-'";
    final String[] li = bisect(s.substring(1), "=");
    assert li.length == 2 : "property should be of the form -x=y or -x.p=y but was [" + s + "]";
    if (li[0].contains("."))
      setInnerProperty(li[0], li[1]);
    else
      setOuterProperty(li[0], li[1]);
  }

  /** @param s
   * @param by
   * @return */
  private static String[] bisect(final String s, final String by) {
    final String[] $ = new String[2];
    final int i = s.indexOf(by);
    $[0] = s.substring(0, i);
    $[1] = s.substring(i + 1);
    return $;
  }

  /** Sets property of the form x=y.
   * @param key
   * @param value */
  private static void setOuterProperty(final String key, final String value) {
    set(key, value);
  }

  /** Sets property of the form x.z=y.
   * @param left
   * @param right */
  private static void setInnerProperty(final String left, final String right) {
    setExternalProperty(left.split("\\.")[0], left.split("\\.")[1], right);
  }

  private static void setExternalProperty(final String cls, final String property, final String value) {
    set(cls, property, value);
  }
}