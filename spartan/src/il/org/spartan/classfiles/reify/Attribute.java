package il.org.spartan.classfiles.reify;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.jetbrains.annotations.*;

/** @author Yossi Gil
 * @since 28 November 2011 */
@Retention(RUNTIME)
public @interface Attribute {
  class Content {
    public final String name;
    public final String value;

    /** Instantiate {@link Content}.
     * @param value JD
     * @param name */
    public Content(final String name, final String value) {
      this.name = name;
      this.value = value;
    }
  }

  class Extractor {
    @NotNull public static List<Content> attributes(@NotNull final Object target) {
      @NotNull final List<Content> $ = new ArrayList<>();
      for (@NotNull final Method ¢ : target.getClass().getMethods())
        if (isAttribute(¢))
          $.add(new Content(¢.getName(), value(target, ¢)));
      return $;
    }
    private static boolean isAttribute(@NotNull final Method ¢) {
      return ¢.getAnnotation(Attribute.class) != null;
    }
    @NotNull private static String value(final Object target, @NotNull final Method m) {
      try {
        return m.invoke(target) + "";
      } catch (@NotNull final IllegalArgumentException $) {
        return "IllegalArgument: " + $.getMessage();
      } catch (@NotNull final IllegalAccessException $) {
        return "IllegalAccess: " + $.getMessage();
      } catch (@NotNull final InvocationTargetException $) {
        return "Exception in call: " + $.getMessage();
      }
    }
  }
}
