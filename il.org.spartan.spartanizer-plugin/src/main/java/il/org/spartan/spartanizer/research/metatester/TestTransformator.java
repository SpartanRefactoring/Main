package il.org.spartan.spartanizer.research.metatester;

import fluent.ly.nil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

import static il.org.spartan.spartanizer.research.metatester.MetaTesterStringUtils.getTemplatedValues;

/** @author Oren Afek
 * @since 5/28/2017. */
public interface TestTransformator {
  static String transformIfPossible(final String ¢) {
    return TestTransformator.transformIfPossible(¢, AssertJTransformator.class);
  }
  static String transformIfPossible(final String s, final Class<?> ofType) {
    return Arrays.stream(ofType.getDeclaredMethods()).filter(λ -> λ.isAnnotationPresent(Order.class))
        .sorted(Comparator.comparingInt(λ -> λ.getAnnotation(Order.class).value())).map(λ -> (String) safeInvoke(λ, s.trim()))
        .filter(λ -> !λ.equals(s.trim())).findFirst().orElse(s);
  }
  static Object safeInvoke(final Method $, final Object o, @SuppressWarnings("unused") final Object... args) {
    try {
      $.setAccessible(true);
      return $.invoke(null, o);
    } catch (IllegalAccessException | InvocationTargetException ignore) {
      return nil.forgetting(ignore);
    }
  }
  static String getReplacerString(final String replacePattern, final String[] args) {
    return String.format(replacePattern, (Object[]) args);
  }
  static String replace(final String $, final String matchPattern, final String replacePattern) {
    try {
      return replace($, matchPattern, replacePattern, naturalsByTemplateString(replacePattern));
    } catch (@SuppressWarnings("unused") final Exception ignore) {
      return $;
    }
  }
  static int[] naturalsByTemplateString(final String template) {
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
  static int[] naturalsInLengthOf(final int i) {
    final int[] $ = new int[i];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = ¢;
    return $;
  }
  static <T> T[] rearange(final T[] original, final T[] $, final int[] order) {
    for (int ¢ = 0; ¢ < order.length; ++¢)
      $[¢] = original[order[¢]];
    return $;
  }
  static String replace(final String s, final String matchPattern, final String replacePattern, final int[] orderOfTemplatedValues) {
    try {
      return s.replaceAll(matchPattern, getReplacerString(replacePattern,
          rearange(getTemplatedValues(s, matchPattern), new String[orderOfTemplatedValues.length], orderOfTemplatedValues)));
    } catch (@SuppressWarnings("unused") final Exception e) {
      return s;
    }
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @interface Order {
    int value();
  }
}
