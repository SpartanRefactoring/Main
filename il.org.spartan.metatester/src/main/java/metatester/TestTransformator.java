package metatester;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

import static metatester.AssertJTransformator.transformations;
import static metatester.aux_layer.MetaTesterStringUtils.getTemplatedValues;

/**
 * Transformations logics for better idiomatic tests.
 * FOR DEMO PURPOSES, only {@link {#AssertJTransformator} AssertJTransformations} are currently enabled.
 *
 * @author Oren Afek
 * @since 5/28/2017.
 */
public interface TestTransformator {
    static String transformIfPossible(final String ¢) {
        return transformIfPossible(transformations, ¢.trim());
    }

    static String transformIfPossible(final Transformation[] transformations, final String s) {
        return Arrays.stream(transformations)
                .map(t -> t.replace(s))
                .filter(x -> !x.trim().equals(s.trim()))
                .findFirst().orElse(s);
    }

    /**
     * Wrapper for invocation of methods using Reflection without worrying about exceptions
     *
     * @param $    method to invoke
     * @param o    target object
     * @param args to $
     * @return $'s return value
     */
    static Object safeInvoke(final Method $, final Object o, @SuppressWarnings("unused") final Object... args) {
        try {
            $.setAccessible(true);
            return $.invoke(null, o);
        } catch (IllegalAccessException | InvocationTargetException ignore) {
            return null;
        }
    }

    /**
     * Combining a formatted string pattern with its arguments.
     *
     * @param replacePattern string pattern
     * @param args           the pattern's arguments.
     * @return the combined string
     */
    static String getReplacerString(final String replacePattern, final String[] args) {
        return String.format(replacePattern, (Object[]) args);
    }

    /**
     * As {@link {{@link #replace(String, String, String, int[])} replace)}}} with the Id permutation (λ -> λ)
     */
    static String replace(final String $, final String matchPattern, final String replacePattern) {
        try {
            return replace($, matchPattern, replacePattern, naturalsByTemplateString(replacePattern));
        } catch (@SuppressWarnings("unused") final Exception ignore) {
            return $;
        }
    }

    static int[] naturalsByTemplateString(final String ¢) {
        final String findStr = "%s";
        int $ = 0, lastIndex = 0;
        while (lastIndex != -1) {
            lastIndex = ¢.indexOf(findStr, lastIndex);
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

    /**
     * Rearranges an array according to a certain permutation.
     *
     * @param original the array to rearange
     * @param $        destination array
     * @param order    permutation
     * @param <T>      JD
     * @return $
     */
    static <T> T[] rearange(final T[] original, final T[] $, final int[] order) {
        for (int ¢ = 0; ¢ < order.length; ++¢)
            $[¢] = original[order[¢]];
        return $;
    }

    /**
     * Replaces a string with a pattern with respcet to the string's original params.
     *
     * @param $                      the original string
     * @param matchPattern           pattern to extract the rightful information
     * @param replacePattern         pattern to replcae with place holders for the rightful information
     * @param orderOfTemplatedValues permutation of the place holders, from their position in the match patten
     *                               to their position in the replcae pattern.
     * @return the replace pattern combined with the rightful information.
     */
    static String replace(final String $, final String matchPattern, final String replacePattern, final int[] orderOfTemplatedValues) {
        try {
            return $.replaceAll(matchPattern, getReplacerString(replacePattern,
                    rearange(getTemplatedValues($, matchPattern).toArray(new String[]{}), new String[orderOfTemplatedValues.length], orderOfTemplatedValues)));
        } catch (@SuppressWarnings("unused") final Exception e) {
            return $;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Order {
        int value();
    }


}
