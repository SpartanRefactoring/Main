package il.org.spartan.Leonidas.plugin.GUI.ToolBoxController;

import il.org.spartan.Leonidas.auxilary_layer.Existence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author Sharon
 * @since 30-06-17.
 */
public class DataConverter<T> {
    public static final DataConverter<String> STRING = new DataConverter<>(s -> s, s -> s);
    public static final DataConverter<Integer> INTEGER = new DataConverter<>(i -> Integer.toString(i), Integer::parseInt);
    public static final DataConverter<Double> DOUBLE = new DataConverter<>(i -> Double.toString(i), Double::parseDouble);
    public static final DataConverter<Float> FLOAT = new DataConverter<>(i -> Float.toString(i), Float::parseFloat);
    public static final DataConverter<Long> LONG = new DataConverter<>(i -> Long.toString(i), Long::parseLong);
    public static final DataConverter<List<String>> STRING_LIST = new DataConverter<>(list -> String.join(",", list),
            s -> "".equals(s) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(s.split("\\s*,\\s*"))));
    public static final DataConverter<Existence> EXISTENCE = new DataConverter<>(Enum::toString, Existence::valueOf);

    private Function<T, String> toString;
    private Function<String, T> fromString;

    public DataConverter(Function<T, String> toString, Function<String, T> fromString) {
        this.toString = toString;
        this.fromString = fromString;
    }

    public static String convert(Object o) {
        //noinspection unchecked
        return String.class.isAssignableFrom(o.getClass()) ? STRING.toString((String) o)
                : Integer.class.isAssignableFrom(o.getClass()) ? INTEGER.toString((Integer) o)
						: Integer.class.isAssignableFrom(o.getClass()) ? DOUBLE.toString((Double) o)
								: Integer.class.isAssignableFrom(o.getClass()) ? FLOAT.toString((Float) o)
										: Integer.class.isAssignableFrom(o.getClass()) ? LONG.toString((Long) o)
												: List.class.isAssignableFrom(o.getClass())
														? STRING_LIST.toString((List<String>) o)
														: Existence.class.isAssignableFrom(o.getClass())
																? EXISTENCE.toString((Existence) o) : "";
	}

    public static Object decode(String s, Class<?> c) {
		return String.class.isAssignableFrom(c) ? STRING.fromString(s)
				: Integer.class.isAssignableFrom(c) ? INTEGER.fromString(s)
						: Double.class.isAssignableFrom(c) ? DOUBLE.fromString(s)
								: Float.class.isAssignableFrom(c) ? FLOAT.fromString(s)
										: Long.class.isAssignableFrom(c) ? LONG.fromString(s)
												: List.class.isAssignableFrom(c) ? STRING_LIST.fromString(s)
														: Existence.class.isAssignableFrom(c) ? EXISTENCE.fromString(s)
																: "";
	}

    public static boolean isSupported(Class<?> c) {
        return String.class.isAssignableFrom(c) ||
                Integer.class.isAssignableFrom(c) ||
                Double.class.isAssignableFrom(c) ||
                Float.class.isAssignableFrom(c) ||
                Long.class.isAssignableFrom(c) ||
                List.class.isAssignableFrom(c) ||
                Existence.class.isAssignableFrom(c);
    }

    public String toString(T t) {
        return toString.apply(t);
    }

    public Object fromString(String s) {
        return fromString.apply(s);
    }
}
