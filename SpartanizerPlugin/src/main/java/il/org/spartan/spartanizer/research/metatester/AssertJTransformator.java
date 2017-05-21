package il.org.spartan.spartanizer.research.metatester;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Oren Afek
 * @since 19.5.17
 */
public class AssertJTransformator {

    public static String transformIfPossible(String s) {
        return Arrays.stream(AssertJTransformations.class.getDeclaredMethods())
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AssertJTransformations.Order.class).value()))
                .map(m -> (String) safeInvoke(m, s))
                .filter(transformed -> !transformed.equals(s)).findFirst().orElse(s);
    }

    private static Object safeInvoke(Method m, Object o, Object... args) {
        try {
            m.setAccessible(true);
            return m.invoke(null, o);
        } catch (IllegalAccessException | InvocationTargetException ignore) {/**/}
        return null;
    }

    private static String getReplacerString(String replacePattern, String[] args) {
        return String.format(replacePattern, (Object[]) args);
    }

    private static String[] getTemplatedValues(String s, String pattern) {
        Matcher m = Pattern.compile(pattern).matcher(s);
        List<String> l = new ArrayList<>();
        // m.find();
        for (int i = 1; i <= m.groupCount() && m.matches(); i++) {
            l.add(m.group(i));
            //m.find();
        }
        String[] a = new String[l.size()];
        return l.toArray(a);
    }

    private static String replace(String s, String matchPattern, String replacePattern) {
        try {
            return replace(s, matchPattern, replacePattern, naturalsByTemplateString(replacePattern));
        } catch (Exception ignore) {/**/}
        return s;
    }

    private static int[] naturalsByTemplateString(String template) {
        String findStr = "%s";
        int count = 0;
        int lastIndex = 0;
        while (lastIndex != -1) {

            lastIndex = template.indexOf(findStr, lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        return naturalsInLengthOf(count);
    }

    private static int[] naturalsInLengthOf(int n) {
        int[] result = new int[n];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }

        return result;
    }

    private static <T> T[] rearange(T[] original, T[] dest, int[] order) {
        for (int i = 0; i < order.length; i++) {
            dest[i] = original[order[i]];
        }
        return dest;
    }

    private static String replace(String s, String matchPattern, String replacePattern, int[] orderOfTemplatedValues) {
        try {
            String[] a = getTemplatedValues(s, matchPattern);
            String[] rearranged = new String[orderOfTemplatedValues.length];
            rearranged = rearange(a, rearranged, orderOfTemplatedValues);
            return s.replaceAll(matchPattern, getReplacerString(replacePattern, rearranged));
        } catch (Exception e) {/**/}
        return s;
    }

    private static class AssertJTransformations {


        @Order(0)
        private static String replaceAssertEquals_isEmpty(String s) {
            //language=RegExp
            return replace(s, "assertEquals\\(0,(.*).size\\(\\)\\);", "assertThat(%s).isEmpty\\(\\);");
        }

        @Order(1)
        private static String replaceAssertEquals_hasSize(String s) {
            //language=RegExp
            return replace(s, "assertEquals\\((.*),(.*).size\\(\\)\\);", "assertThat\\(%s\\).hasSize\\(%s\\);", new int[]{1, 0});
        }

        @Order(2)
        private static String replaceAssertEquals_isEqualTo(String s) {
            //language=RegExp
            return replace(s, "assertEquals\\((.*),(.*)\\);", "assertThat\\(%s\\).isEqualTo\\(%s\\);", new int[]{1, 0});
        }

        @Order(3)
        private static String replaceAssertNull_isNull(String s) {
            //language=RegExp
            return replace(s, "assertNull\\((.*)\\);", "assertThat\\(%s\\).isNull\\(\\);");
        }

        @Order(4)
        private static String replaceAssertNotNull_isNotNull(String s) {
            //language=RegExp
            return replace(s, "assertNotNull\\((.*)\\);", "assertThat\\(%s\\).isNotNull\\(\\);");
        }

        @Order(5)
        private static String replaceAssertTrue_isTrue(String s) {
            //language=RegExp
            return replace(s, "assertTrue\\((.*)\\);", "assertThat\\(%s\\).isTrue\\(\\);");
        }

        @Order(6)
        private static String replaceAssertFalse_isFalse(String s) {
            //language=RegExp
            return replace(s, "assertFalse\\((.*)\\);", "assertThat\\(%s\\).isFalse\\(\\);");
        }

        @Target(ElementType.METHOD)
        @Retention(RetentionPolicy.RUNTIME)
        private @interface Order {
            int value();
        }

    }
}