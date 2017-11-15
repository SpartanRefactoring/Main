package metatester.aux_layer;

import org.intellij.lang.annotations.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String utilities.
 *
 * @author Oren Afek
 * @since 5/28/2017.
 */
public class MetaTesterStringUtils {
    public static List<String> getTemplatedValues(final String s, final String pattern) {
        final Matcher m = Pattern.compile(pattern).matcher(s);
        final List<String> $ = new ArrayList<>();
        for (int ¢ = 1; ¢ <= m.groupCount() && m.matches(); ++¢)
            $.add(m.group(¢));
        return Arrays.asList($.toArray(new String[$.size()]));
    }

    public static List<String> getTemplatedValues(final String s, final List<String> patterns) {
        return patterns.stream()
                .map(λ -> getTemplatedValues(s, λ))
                .filter(λ -> !λ.isEmpty()).findFirst().orElse(Collections.emptyList());
    }


    public static <T> String removeBraces(final List<T> ¢) {
        final StringBuilder $ = new StringBuilder();
        ¢.stream().forEach(e -> $.append(e + ", "));
        return $.substring(0, $.length() - 2);
    }
}
