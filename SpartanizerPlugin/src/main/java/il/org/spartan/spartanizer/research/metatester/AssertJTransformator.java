package il.org.spartan.spartanizer.research.metatester;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.*;

import fluent.ly.*;

/** @author Oren Afek
 * @since 19.5.17 */
public class AssertJTransformator {
  public static String transformIfPossible(final String s) {
    return Arrays.stream(AssertJTransformations.class.getDeclaredMethods())
        .sorted(Comparator.comparingInt(λ -> λ.getAnnotation(AssertJTransformations.Order.class).value())).map(λ -> (String) safeInvoke(λ, s))
        .filter(λ -> !λ.equals(s)).findFirst().orElse(s);
  }
  private static Object safeInvoke(final Method $, final Object o, final Object... args) {
    try {
      $.setAccessible(true);
      return $.invoke(null, o);
    } catch (IllegalAccessException | InvocationTargetException ignore) {
      return nil.forgetting(ignore);
    }
  }
  private static String getReplacerString(final String replacePattern, final String[] args) {
    return String.format(replacePattern, (Object[]) args);
  }
  private static String[] getTemplatedValues(final String s, final String pattern) {
    final Matcher m = Pattern.compile(pattern).matcher(s);
    final List<String> $ = new ArrayList<>();
    // m.find();
    for (int ¢ = 1; ¢ <= m.groupCount() && m.matches(); ++¢)
      $.add(m.group(¢));
    return $.toArray(new String[$.size()]);
  }
  static String replace(final String $, final String matchPattern, final String replacePattern) {
    try {
      return replace($, matchPattern, replacePattern, naturalsByTemplateString(replacePattern));
    } catch (@SuppressWarnings("unused") final Exception ignore) {
      return $;
    }
  }
  private static int[] naturalsByTemplateString(final String template) {
    final String findStr = "%s";
    int $ = 0, lastIndex = 0;
    while (lastIndex != -1) {
      lastIndex = template.indexOf(findStr, lastIndex);
      if (lastIndex != -1) {
        ++$;
        lastIndex += findStr.length();
      }
    }
    return naturalsInLengthOf($);
  }
  private static int[] naturalsInLengthOf(final int i) {
    final int[] $ = new int[i];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = ¢;
    return $;
  }
  private static <T> T[] rearange(final T[] original, final T[] $, final int[] order) {
    for (int ¢ = 0; ¢ < order.length; ++¢)
      $[¢] = original[order[¢]];
    return $;
  }
  private static String replace(final String s, final String matchPattern, final String replacePattern, final int[] orderOfTemplatedValues) {
    try {
      final String[] a = getTemplatedValues(s, matchPattern);
      String[] $ = new String[orderOfTemplatedValues.length];
      $ = rearange(a, $, orderOfTemplatedValues);
      return s.replaceAll(matchPattern, getReplacerString(replacePattern, $));
    } catch (@SuppressWarnings("unused") final Exception e) {
      return s;
    }
  }

  private static class AssertJTransformations {
    @Order(0) private static String replaceAssertEquals_isEmpty(final String ¢) {
      // language=RegExp
      return replace(¢, "assertEquals\\(0,(.*).size\\(\\)\\);", "assertThat(%s).isEmpty\\(\\);");
    }
    @Order(1) private static String replaceAssertEquals_hasSize(final String ¢) {
      // language=RegExp
      return replace(¢, "assertEquals\\((.*),(.*).size\\(\\)\\);", "assertThat\\(%s\\).hasSize\\(%s\\);", new int[] { 1, 0 });
    }
    @Order(2) private static String replaceAssertEquals_isEqualTo(final String ¢) {
      // language=RegExp
      return replace(¢, "assertEquals\\((.*),(.*)\\);", "assertThat\\(%s\\).isEqualTo\\(%s\\);", new int[] { 1, 0 });
    }
    @Order(3) private static String replaceAssertNull_isNull(final String ¢) {
      // language=RegExp
      return replace(¢, "assertNull\\((.*)\\);", "assertThat\\(%s\\).isNull\\(\\);");
    }
    @Order(4) private static String replaceAssertNotNull_isNotNull(final String ¢) {
      // language=RegExp
      return replace(¢, "assertNotNull\\((.*)\\);", "assertThat\\(%s\\).isNotNull\\(\\);");
    }
    @Order(5) private static String replaceAssertTrue_isTrue(final String ¢) {
      // language=RegExp
      return replace(¢, "assertTrue\\((.*)\\);", "assertThat\\(%s\\).isTrue\\(\\);");
    }
    @Order(6) private static String replaceAssertFalse_isFalse(final String ¢) {
      // language=RegExp
      return replace(¢, "assertFalse\\((.*)\\);", "assertThat\\(%s\\).isFalse\\(\\);");
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface Order {
      int value();
    }
  }
}