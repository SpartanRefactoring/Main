package il.org.spartan.spartanizer.research.metatester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Oren Afek
 * @since 5/28/2017.
 */
public class MetaTesterStringUtils {

    public static String[] getTemplatedValues(final String s, final String pattern) {
        final Matcher m = Pattern.compile(pattern).matcher(s);
        final List<String> $ = new ArrayList<>();
        // m.find();
        for (int ¢ = 1; ¢ <= m.groupCount() && m.matches(); ++¢)
            $.add(m.group(¢));
        return $.toArray(new String[$.size()]);
    }

    public static String getTemplatedValue(final String s, final String... patterns) {
        return Arrays.stream(patterns)
                .map(p -> getTemplatedValues(s, p))
                .filter(c -> c.length >= 1)
                .map(c -> c[0])
                .findFirst().orElse("");
    }

    public static String getTemplatedValue(final String s, final String pattern) {
        String[] candidates = getTemplatedValues(s, pattern);
        return candidates.length > 0 ? candidates[0] : "";
    }
}
